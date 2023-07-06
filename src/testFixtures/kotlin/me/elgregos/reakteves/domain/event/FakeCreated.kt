package me.elgregos.reakteves.domain.event

import com.fasterxml.jackson.databind.JsonNode
import me.elgregos.reakteves.domain.JsonConvertible
import me.elgregos.reakteves.libs.nowUTC
import java.time.LocalDateTime
import java.util.*

sealed class FakeEvent(
    id: UUID,
    sequenceNum: Long?,
    version: Int,
    createdAt: LocalDateTime,
    createdBy: UUID,
    eventType: String,
    aggregateId: UUID,
    event: JsonNode
) : Event<UUID, UUID>(
    id, sequenceNum, version, createdAt, createdBy, eventType, aggregateId, event
), JsonConvertible {

    data class FakeCreated(
        val eventId: UUID = UUID.randomUUID(),
        override val sequenceNum: Long? = null,
        override val version: Int,
        override val createdAt: LocalDateTime = nowUTC(),
        override val createdBy: UUID,
        override val aggregateId: UUID = UUID.randomUUID(),
        override val event: JsonNode
    ) : FakeEvent(
        eventId,
        sequenceNum,
        version,
        createdAt,
        createdBy,
        FakeCreated::class.simpleName!!,
        aggregateId,
        event
    )

    data class FakeUpdated(
        val eventId: UUID = UUID.randomUUID(),
        override val sequenceNum: Long? = null,
        override val version: Int,
        override val createdAt: LocalDateTime = nowUTC(),
        override val createdBy: UUID,
        override val aggregateId: UUID = UUID.randomUUID(),
        override val event: JsonNode
    ) : FakeEvent(
        eventId,
        sequenceNum,
        version,
        createdAt,
        createdBy,
        FakeUpdated::class.simpleName!!,
        aggregateId,
        event
    )

}

fun fakeCreatedEvent(eventId: UUID, version: Int, createdBy: UUID, aggregateId: UUID, event: JsonNode) =
    FakeEvent.FakeCreated(
        eventId = eventId,
        version = version,
        createdBy = createdBy,
        aggregateId = aggregateId,
        event = event
    )

fun fakeUpdatedEvent(eventId: UUID, version: Int, createdBy: UUID, aggregateId: UUID, event: JsonNode) =
    FakeEvent.FakeUpdated(
        eventId = eventId,
        version = version,
        createdBy = createdBy,
        aggregateId = aggregateId,
        event = event
    )