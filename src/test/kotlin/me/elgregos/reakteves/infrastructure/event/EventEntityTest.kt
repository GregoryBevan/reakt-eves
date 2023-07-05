package me.elgregos.reakteves.infrastructure.event

import assertk.assertThat
import assertk.assertions.isEqualTo
import me.elgregos.reakteves.domain.event.FakeEvent
import me.elgregos.reakteves.domain.event.fakeCreatedEvent
import me.elgregos.reakteves.libs.genericObjectMapper
import java.util.*
import kotlin.test.Test

class EventEntityTest {

    @Test
    fun `should convert event entity to event based on class`() {
        val eventId = UUID.randomUUID()
        val event = genericObjectMapper.createObjectNode().put("field1", "value1")
        val fakeCreated = fakeCreatedEvent(eventId, 1, UUID.randomUUID(), UUID.randomUUID(), event)
        val fakeEventEntity = FakeEventEntity(fakeCreated.eventId, fakeCreated.sequenceNum, fakeCreated.version, fakeCreated.createdAt, fakeCreated.createdBy, fakeCreated.eventType, fakeCreated.aggregateId, fakeCreated.event)
        assertThat(fakeEventEntity.toEvent(FakeEvent::class)).isEqualTo(fakeCreated)
    }

    @Test
    fun `should convert event to event entity based on classes`() {
        val eventId = UUID.randomUUID()
        val event = genericObjectMapper.createObjectNode().put("field1", "value1")
        val fakeCreated = fakeCreatedEvent(eventId, 1, UUID.randomUUID(), UUID.randomUUID(), event)
        val fakeEventEntity = FakeEventEntity(fakeCreated.eventId, fakeCreated.sequenceNum, fakeCreated.version, fakeCreated.createdAt, fakeCreated.createdBy, fakeCreated.eventType, fakeCreated.aggregateId, fakeCreated.event)
        assertThat(EventEntity.fromEvent(fakeCreated, FakeEventEntity::class)).isEqualTo(fakeEventEntity)
    }
}