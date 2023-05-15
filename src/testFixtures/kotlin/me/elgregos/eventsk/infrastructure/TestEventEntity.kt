package me.elgregos.eventsk.infrastructure

import com.fasterxml.jackson.databind.JsonNode
import me.elgregos.eventsk.domain.TestEvent
import me.elgregos.eventsk.libs.nowUTC
import org.springframework.data.relational.core.mapping.Table
import java.time.LocalDateTime
import java.util.*

@Table(name = "test_event")
data class TestEventEntity(
    @get:JvmName("id") val id: UUID,
    override val sequenceNum: Long? = null,
    override val version: Int = 1,
    override val createdAt: LocalDateTime = nowUTC(),
    override val createdBy: UUID,
    override val aggregateId: UUID = UUID.randomUUID(),
    override val eventType:String,
    override val event: JsonNode
) : EventEntity<TestEvent, UUID>(
    id,
    sequenceNum,
    version,
    createdAt,
    createdBy,
    eventType,
    aggregateId,
    event
) {

    companion object {
        fun fromEvent(testEvent: TestEvent) =
            TestEventEntity(
                id = testEvent.id,
                createdAt = testEvent.createdAt,
                createdBy = testEvent.createdBy,
                aggregateId = testEvent.aggregateId,
                eventType = testEvent.eventType,
                event = testEvent.event
            )
    }
}
