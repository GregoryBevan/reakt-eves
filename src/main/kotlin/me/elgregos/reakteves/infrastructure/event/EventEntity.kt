package me.elgregos.reakteves.infrastructure.event

import com.fasterxml.jackson.databind.JsonNode
import me.elgregos.reakteves.domain.event.Event
import org.springframework.boot.autoconfigure.security.SecurityProperties.User
import org.springframework.data.annotation.Id
import org.springframework.data.annotation.Transient
import org.springframework.data.domain.Persistable
import java.time.LocalDateTime
import java.util.*
import kotlin.reflect.KClass
import kotlin.reflect.full.primaryConstructor


open class EventEntity<E : Event<ID, out UserID>, ID, out UserID>(
    @Id private val id: ID,
    open val sequenceNum: Long?,
    open val version: Int,
    open val createdAt: LocalDateTime,
    open val createdBy: UserID,
    open val eventType: String,
    open val aggregateId: ID,
    open val event: JsonNode
) : Persistable<ID> {

    @Transient
    private var isNew = false

    @Transient
    override fun isNew() = isNew

    fun markNew() {
        isNew = true
    }

    fun toEvent(eventClass: Class<E>, idClass: Class<ID>): E =
        eventClass.permittedSubclasses
            .first { it.simpleName == eventType }
            .let {
                it.getConstructor(
                    UUID::class.java,
                    Long::class.javaObjectType,
                    Int::class.javaObjectType,
                    LocalDateTime::class.java,
                    idClass,
                    idClass,
                    JsonNode::class.java
                ).newInstance(
                    getId(),
                    sequenceNum,
                    version,
                    createdAt,
                    createdBy,
                    aggregateId,
                    event
                ) as E
            }

    fun toEvent(eventClass: KClass<E>): E =
        eventClass.sealedSubclasses
            .first { it.simpleName == eventType }
            .let {
                it.primaryConstructor?.call(
                    getId(),
                    sequenceNum,
                    version,
                    createdAt,
                    createdBy,
                    aggregateId,
                    event
                ) as E
            }

    override fun getId() = id

    companion object {
        fun <EE : EventEntity<E, ID, UserID>, E : Event<ID, UserID>, ID : Any, UserID > fromEvent(
            event: E,
            eventEntityClass: KClass<EE>
        ): EE = eventEntityClass.primaryConstructor?.call(
                event.id,
                event.sequenceNum,
                event.version,
                event.createdAt,
                event.createdBy,
                event.eventType,
                event.aggregateId,
                event.event
            ) as EE

    }

}




