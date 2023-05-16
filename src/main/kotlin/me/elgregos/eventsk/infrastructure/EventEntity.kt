package me.elgregos.eventsk.infrastructure

import com.fasterxml.jackson.databind.JsonNode
import me.elgregos.eventsk.domain.Event
import org.springframework.data.annotation.Id
import org.springframework.data.annotation.Transient
import org.springframework.data.domain.Persistable
import java.time.LocalDateTime
import java.util.*
import kotlin.reflect.KClass
import kotlin.reflect.full.primaryConstructor


open class EventEntity<E : Event<IdType>, IdType>(
    @Id private val id: UUID,
    open val sequenceNum: Long?,
    open val version: Int,
    open val createdAt: LocalDateTime,
    open val createdBy: IdType,
    open val eventType: String,
    open val aggregateId: IdType,
    open val event: JsonNode
) : Persistable<UUID> {

    @Transient
    private var isNew = false

    @Transient
    override fun isNew() = isNew

    fun markNew() {
        isNew = true
    }

    fun toEvent(eventClass: Class<E>, idClass: Class<IdType>): E =
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

    inline fun <reified E : Event<IdType>, reified IdType> toEvent(): E {
        return E::class.java.permittedSubclasses
            .first { it.simpleName == eventType }
            .let {
                it.getConstructor(
                    UUID::class.java,
                    Long::class.javaObjectType,
                    Int::class.java,
                    LocalDateTime::class.java,
                    IdType::class.java,
                    IdType::class.java,
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
    }

    override fun getId() = id

    companion object {
        inline fun <reified EE : EventEntity<E, IdType>, reified E : Event<IdType>, reified IdType> fromEvent(event: E): EE =
            EE::class.java.getConstructor(
                UUID::class.java,
                Long::class.javaObjectType,
                Int::class.java,
                LocalDateTime::class.java,
                IdType::class.java,
                String::class.java,
                IdType::class.java,
                JsonNode::class.java
            ).newInstance(
                event.id,
                event.sequenceNum,
                event.version,
                event.createdAt,
                event.createdBy,
                event.eventType,
                event.aggregateId,
                event.event
            )

        fun <EE : EventEntity<E, IdType>, E : Event<IdType>, IdType : Any> fromEvent(
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




