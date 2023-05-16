package me.elgregos.eventsk.domain

import assertk.assertThat
import assertk.assertions.isEqualTo
import io.mockk.every
import io.mockk.mockk
import me.elgregos.eventsk.libs.genericObjectMapper
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import reactor.test.StepVerifier
import java.util.*
import kotlin.test.BeforeTest
import kotlin.test.Test

internal class AggregateTest {

    private lateinit var eventStore: EventStore<FakeEvent, UUID>

    @BeforeTest
    fun setUp() {
        eventStore = mockk()
    }

    @Test
    fun `should add one new event`() {
        val aggregateId = UUID.fromString("63a71133-30c0-4de8-ad4b-da05ebf98e15")
        val eventId = UUID.fromString("b7abd716-fbb2-491e-aa4e-5f5174e889ad")
        val createdBy = UUID.fromString("ff7474d7-c91d-458a-b7a1-1a0a67a430f3")

        every { eventStore.loadAllEvents(aggregateId) } returns Flux.empty()

        val aggregateExample = AggregateExample(aggregateId, eventStore)

        aggregateExample.generateOneEvent(eventId, createdBy)
            .`as`(StepVerifier::create)
            .assertNext {
                assertThat(it).isEqualTo(
                    fakeCreatedEvent(eventId, 1, createdBy, aggregateId, genericObjectMapper.createObjectNode().put("field1", "value1"))
                        .copy(createdAt = it.createdAt)
                )
            }
            .verifyComplete()

        assertThat(aggregateExample.newEvents().size).isEqualTo(1)
    }

    @Test
    fun `should add two new event with correct versions`() {
        val aggregateId = UUID.fromString("63a71133-30c0-4de8-ad4b-da05ebf98e15")
        val createdBy = UUID.fromString("ff7474d7-c91d-458a-b7a1-1a0a67a430f3")
        val eventId1 = UUID.fromString("b7abd716-fbb2-491e-aa4e-5f5174e889ad")
        val eventId2 = UUID.fromString("23bee42c-801c-41e3-b898-4a23f269dec5")
        every { eventStore.loadAllEvents(aggregateId) } returns Flux.empty()

        val aggregateExample = AggregateExample(aggregateId, eventStore)

        aggregateExample.generateOneEvent(eventId1, createdBy)
            .`as` { StepVerifier.create(it) }
            .assertNext {
                assertThat(it).isEqualTo(
                    fakeCreatedEvent(eventId1, 1, createdBy, aggregateId, genericObjectMapper.createObjectNode().put("field1", "value1"))
                        .copy(createdAt = it.createdAt)
                )
            }
            .verifyComplete()

        aggregateExample.generateOneEvent(eventId2, createdBy)
            .`as` { StepVerifier.create(it) }
            .assertNext {
                assertThat(it).isEqualTo(
                    fakeCreatedEvent(eventId2, 2, createdBy, aggregateId, genericObjectMapper.createObjectNode().put("field2", "value2"))
                        .copy(createdAt = it.createdAt)
                )
            }
            .verifyComplete()

        assertThat(aggregateExample.newEvents().size).isEqualTo(2)
    }

    @Test
    fun `should throw an exception for new event with bad version`() {
        val aggregateId = UUID.randomUUID()
        every { eventStore.loadAllEvents(aggregateId) } returns Flux.empty()

        val aggregateExample = AggregateExample(aggregateId, eventStore)

        aggregateExample.generateOneEventWithBadVersion(UUID.randomUUID(), UUID.randomUUID())
            .`as` { StepVerifier.create(it) }
            .expectErrorMatches { throwable ->
                throwable is EventVersionException && throwable.message == "New event version (2) does not match expected next version (1)"
            }
            .verify()

    }

    @Test
    fun `should return empty json if no events previously stored`() {
        val aggregateId = UUID.randomUUID()
        every { eventStore.loadAllEvents(aggregateId) } returns Flux.empty()

        val aggregateExample = AggregateExample(aggregateId, eventStore)

        aggregateExample.previousState()
            .`as` { StepVerifier.create(it) }
            .assertNext { assertThat(it).isEqualTo(genericObjectMapper.createObjectNode()) }
            .verifyComplete()
    }

    @Test
    fun `should rebuild previous state from events already stored`() {
        val aggregateId = UUID.randomUUID()
        every { eventStore.loadAllEvents(aggregateId) } returns storedEvents(aggregateId)

        val aggregateExample = AggregateExample(aggregateId, eventStore)

        aggregateExample.previousState()
            .`as` { StepVerifier.create(it) }
            .assertNext {
                assertThat(it).isEqualTo(
                    genericObjectMapper.createObjectNode().put("field1", "value3").put("field2", "value2")
                )
            }
            .verifyComplete()
    }

    @Test
    fun `should rebuild previous state from events already stored and new events`() {
        val aggregateId = UUID.randomUUID()
        every { eventStore.loadAllEvents(aggregateId) } returns storedEvents(aggregateId)

        val aggregateExample = AggregateExample(aggregateId, eventStore)

        aggregateExample.generateOneEvent(UUID.randomUUID(), UUID.randomUUID())
            .flatMap { aggregateExample.previousState() }
            .`as` { StepVerifier.create(it) }
            .assertNext {
                assertThat(it).isEqualTo(
                    genericObjectMapper.createObjectNode()
                        .put("field1", "value3")
                        .put("field2", "value2")
                        .put("field4", "value4")
                )
            }
            .verifyComplete()
    }

    @Test
    fun `should get next version equals 1 when no previous events stored`() {
        val aggregateId = UUID.randomUUID()
        every { eventStore.loadAllEvents(aggregateId) } returns Flux.empty()

        val aggregateExample = AggregateExample(aggregateId, eventStore)

        aggregateExample.nextVersion()
            .`as` { StepVerifier.create(it) }
            .assertNext { assertThat(it).isEqualTo(1) }
            .verifyComplete()
    }

    @Test
    fun `should get next version equals 3 when three previous events stored`() {
        val aggregateId = UUID.randomUUID()
        every { eventStore.loadAllEvents(aggregateId) } returns storedEvents(aggregateId)

        val aggregateExample = AggregateExample(aggregateId, eventStore)

        aggregateExample.nextVersion()
            .`as` { StepVerifier.create(it) }
            .assertNext { assertThat(it).isEqualTo(4) }
            .verifyComplete()
    }

    private fun storedEvents(aggregateId: UUID): Flux<FakeEvent> = Flux.fromIterable(
        listOf(
            fakeCreatedEvent(UUID.randomUUID(),1, UUID.fromString("ff7474d7-c91d-458a-b7a1-1a0a67a430f3"), aggregateId, genericObjectMapper.createObjectNode().put("field1", "value1")),
            fakeCreatedEvent(UUID.randomUUID(),2, UUID.fromString("ff7474d7-c91d-458a-b7a1-1a0a67a430f3"), aggregateId, genericObjectMapper.createObjectNode().put("field2", "value2")),
            fakeCreatedEvent(UUID.randomUUID(), 3, UUID.fromString("ff7474d7-c91d-458a-b7a1-1a0a67a430f3"), aggregateId, genericObjectMapper.createObjectNode().put("field1", "value3"))
        )
    )

}

internal class AggregateExample(
    private val aggregateId: UUID,
    eventStore: EventStore<FakeEvent, UUID>
) : JsonAggregate<FakeEvent, UUID>(aggregateId, eventStore) {

    fun generateOneEvent(eventId: UUID, createdBy: UUID) =
        nextVersion()
            .map {
                fakeCreatedEvent(eventId, it, createdBy, aggregateId, genericObjectMapper.createObjectNode().put("field$it", "value$it"))
            }
            .flatMap { applyNewEvent(it) }

    fun generateOneEventWithBadVersion(eventId: UUID, createdBy: UUID) =
        Mono.just(
            fakeCreatedEvent(eventId, 2, createdBy, aggregateId, genericObjectMapper.createObjectNode().put("field1", "value1"))
        )
            .flatMap { applyNewEvent(it) }
}

