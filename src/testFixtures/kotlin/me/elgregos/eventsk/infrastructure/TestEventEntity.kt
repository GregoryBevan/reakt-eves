package me.elgregos.eventsk.infrastructure

import com.fasterxml.jackson.databind.JsonNode
import me.elgregos.eventsk.domain.JsonConvertible
import me.elgregos.eventsk.domain.TestEvent
import me.elgregos.eventsk.libs.nowUTC
import org.springframework.data.annotation.PersistenceCreator
import org.springframework.data.annotation.Transient
import org.springframework.data.relational.core.mapping.Table
import java.time.LocalDateTime
import java.util.*

@Table(name = "test_event")
data class TestEventEntity(
    @Transient val testEventId: UUID,
    override val sequenceNum: Long? = null,
    override val version: Int = 1,
    override val createdAt: LocalDateTime = nowUTC(),
    override val createdBy: UUID,
    override val aggregateId: UUID = UUID.randomUUID(),
    override val event: JsonNode
) : EventEntity<TestEvent, UUID>(
    testEventId,
    sequenceNum,
    version,
    createdAt,
    createdBy,
    TestEvent::class.java.simpleName,
    aggregateId,
    event
) {

    @PersistenceCreator
    constructor(
        id: UUID,
        sequenceNum: Long,
        version: Int,
        createdAt: LocalDateTime,
        createdBy: UUID,
        aggregateId: UUID,
        eventType:String,
        event: JsonNode
    ) : this(id, sequenceNum, version, createdAt, createdBy, aggregateId, event)

    override fun toEvent() = JsonConvertible.fromJson(event, TestEvent::class.java)

    companion object {
        fun fromEvent(testEvent: TestEvent) =
            TestEventEntity(
                testEventId = testEvent.id,
                createdAt = testEvent.createdAt,
                createdBy = testEvent.createdBy,
                aggregateId = testEvent.aggregateId,
                event = testEvent.event
            )
    }
}
