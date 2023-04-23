package me.elgregos.eventsk

import com.fasterxml.jackson.databind.JsonNode
import me.elgregos.eventsk.libs.nowUTC
import java.time.LocalDateTime
import java.util.*

data class TestEvent(
    val eventId: UUID = UUID.randomUUID(),
    override val sequenceNum: Long = 2L,
    override val version: Int,
    override val createdAt: LocalDateTime = nowUTC(),
    override val createdBy: UUID,
    override val eventType: String = "Test",
    override val aggregateId: UUID = UUID.randomUUID(),
    override val event: JsonNode
) : Event<UUID>(eventId, sequenceNum, version, createdAt, createdBy, eventType, aggregateId, event)

fun testEvent(eventId: UUID, version: Int, createdBy: UUID, aggregateId: UUID, event: JsonNode) = TestEvent(
    eventId = eventId,
    version = version,
    createdBy = createdBy,
    aggregateId = aggregateId,
    event = event
)