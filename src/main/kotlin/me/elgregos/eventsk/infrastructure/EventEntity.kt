package me.elgregos.eventsk.infrastructure

import com.fasterxml.jackson.databind.JsonNode
import me.elgregos.eventsk.domain.Event
import org.springframework.data.annotation.Id
import org.springframework.data.annotation.Transient
import org.springframework.data.domain.Persistable
import java.time.LocalDateTime
import java.util.*


abstract class EventEntity<E : Event<IdType>, IdType>(
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

    fun toEvent(clazz: Class<E>): E =
        clazz.permittedSubclasses
        .first { it.simpleName == eventType }
        .let {
            it.declaredConstructors[0]!!.newInstance(
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
                it.declaredConstructors[0]!!.newInstance(
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
}



