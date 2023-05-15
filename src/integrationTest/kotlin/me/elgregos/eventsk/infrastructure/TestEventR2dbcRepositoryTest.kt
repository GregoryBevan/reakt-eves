package me.elgregos.eventsk.infrastructure

import assertk.assertAll
import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isNotNull
import com.fasterxml.jackson.databind.node.ObjectNode
import me.elgregos.eventsk.config.BaseIntegrationTest
import me.elgregos.eventsk.domain.TestEvent
import me.elgregos.eventsk.domain.testCreatedEvent
import me.elgregos.eventsk.infrastructure.TestEventEntity.Companion.fromEvent
import me.elgregos.eventsk.libs.genericObjectMapper
import org.springframework.beans.factory.annotation.Autowired
import reactor.test.StepVerifier
import java.util.*
import kotlin.test.Test

internal class TestEventR2dbcRepositoryTest: BaseIntegrationTest() {

    @Autowired
    lateinit var testEventR2dbcRepository: TestEventR2dbcRepository

    @Test
    fun `should store new test event`() {
        val event = genericObjectMapper.createObjectNode().put("field1", "value1")
        val eventId = UUID.randomUUID()
        val testEvent = testCreatedEvent(eventId, 1, UUID.randomUUID(), UUID.randomUUID(), event)
        testEventR2dbcRepository.insert(TestEventEntity(id = testEvent.id, createdAt = testEvent.createdAt, createdBy = testEvent.createdBy, aggregateId = testEvent.aggregateId, eventType = testEvent.eventType, event = testEvent.event))
            .flatMap { testEventR2dbcRepository.findById(it.id) }
            .`as` { StepVerifier.create(it) }
            .assertNext { checkEventEntity(it, eventId, event) }
            .verifyComplete()
    }

    @Test
    fun `should store new test events`() {
        val eventId1 = UUID.randomUUID()
        val event1 = genericObjectMapper.createObjectNode().put("field1", "value1")
        val testEvent1 = testCreatedEvent(eventId1, 1, UUID.randomUUID(), UUID.randomUUID(), event1)
        val eventId2 = UUID.randomUUID()
        val event2 = genericObjectMapper.createObjectNode().put("field2", "value2")
        val testEvent2 = testCreatedEvent(eventId2, 1, UUID.randomUUID(), UUID.randomUUID(), event2)
        val eventId3 = UUID.randomUUID()
        val event3 = genericObjectMapper.createObjectNode().put("field3", "value3")
        val testEvent3 = testCreatedEvent(eventId3, 1, UUID.randomUUID(), UUID.randomUUID(), event3)
        val eventId4 = UUID.randomUUID()
        val event4 = genericObjectMapper.createObjectNode().put("field4", "value4")
        val testEvent4 = testCreatedEvent(eventId4, 1, UUID.randomUUID(), UUID.randomUUID(), event4)

        testEventR2dbcRepository.insertAll(listOf(fromEvent(testEvent1), fromEvent(testEvent2), fromEvent(testEvent3), fromEvent(testEvent4)))
            .flatMap { testEventR2dbcRepository.findById(it.id) }
            .`as` { StepVerifier.create(it) }
            .assertNext { checkEventEntity(it, eventId1, event1) }
            .assertNext { checkEventEntity(it, eventId2, event2) }
            .assertNext { checkEventEntity(it, eventId3, event3) }
            .assertNext { checkEventEntity(it, eventId4, event4) }
            .verifyComplete()
    }

    private fun checkEventEntity(
        it: TestEventEntity,
        eventId: UUID?,
        event: ObjectNode?
    ) {
        assertAll {
            assertThat(it.id).isEqualTo(eventId)
            assertThat(it.sequenceNum).isNotNull()
            assertThat(it.event).isEqualTo(event)
        }
    }
}


interface TestEventR2dbcRepository: EventEntityRepository<TestEventEntity, TestEvent, UUID>