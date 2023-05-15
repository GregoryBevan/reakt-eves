package me.elgregos.eventsk.infrastructure

import assertk.assertThat
import assertk.assertions.isEqualTo
import me.elgregos.eventsk.domain.TestEvent
import me.elgregos.eventsk.domain.testCreatedEvent
import me.elgregos.eventsk.libs.genericObjectMapper
import java.util.*
import kotlin.test.Test

class EventEntityTest {

    @Test
    fun shouldConvertEventEntityToEvent() {
        val eventId = UUID.randomUUID()
        val event = genericObjectMapper.createObjectNode().put("field1", "value1")
        val testEvent = testCreatedEvent(eventId, 1, UUID.randomUUID(), UUID.randomUUID(), event)
        val testEventEntity = TestEventEntity.fromEvent(testEvent)
        assertThat(testEventEntity.toEvent<TestEvent, UUID>()).isEqualTo(testEvent)
    }
}