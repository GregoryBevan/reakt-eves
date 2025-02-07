package me.elgregos.reakteves.infrastructure.event

import assertk.assertThat
import assertk.assertions.isEqualTo
import me.elgregos.reakteves.config.BaseIntegrationTest
import me.elgregos.reakteves.domain.event.EventStore
import me.elgregos.reakteves.domain.event.FakeEvent
import me.elgregos.reakteves.domain.event.fakeCreatedEvent
import me.elgregos.reakteves.domain.event.fakeUpdatedEvent
import me.elgregos.reakteves.libs.genericObjectMapper
import me.elgregos.reakteves.libs.uuidV7
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import reactor.test.StepVerifier
import java.util.*

internal class DefaultEventStoreTest: BaseIntegrationTest() {

    @Autowired
    private lateinit var fakeEventStore: EventStore<FakeEvent, UUID, UUID>

    @Test
    fun `should save an event`() {
        val fakeCreated = fakeCreatedEvent(
            uuidV7(),
            1,
            UUID.randomUUID(),
            UUID.randomUUID(),
            genericObjectMapper.createObjectNode().put("root", "definedValue")
        )
        fakeEventStore.save(fakeCreated)
            .`as`(StepVerifier::create)
            .assertNext{ assertThat(it).isEqualTo(fakeCreated) }
            .verifyComplete()
    }

    @Test
    fun `should save list of events`() {
        val fakeCreated1 = fakeCreatedEvent(
            UUID.randomUUID(),
            1,
            UUID.randomUUID(),
            UUID.randomUUID(),
            genericObjectMapper.createObjectNode().put("root", "definedValue")
        )
        val fakeUpdated1 = fakeUpdatedEvent(
            UUID.randomUUID(),
            2,
            fakeCreated1.createdBy,
            fakeCreated1.aggregateId,
            genericObjectMapper.createObjectNode().put("root", "updatedValue")
        )
        val fakeCreated2 = fakeCreatedEvent(
            UUID.randomUUID(),
            1,
            UUID.randomUUID(),
            UUID.randomUUID(),
            genericObjectMapper.createObjectNode().put("root", "definedValue")
        )

        fakeEventStore.saveAll(listOf(fakeCreated1, fakeUpdated1, fakeCreated2))
            .`as` { StepVerifier.create(it) }
            .expectNext(fakeCreated1)
            .expectNext(fakeUpdated1)
            .expectNext(fakeCreated2)
            .verifyComplete()
    }

    @Test
    fun `should load all events for a given aggregate id`() {
        val fakeCreated1 = fakeCreatedEvent(
            uuidV7(),
            1,
            UUID.randomUUID(),
            UUID.randomUUID(),
            genericObjectMapper.createObjectNode().put("root", "definedValue")
        )
        val fakeUpdated1 = fakeUpdatedEvent(
            uuidV7(),
            2,
            fakeCreated1.createdBy,
            fakeCreated1.aggregateId,
            genericObjectMapper.createObjectNode().put("root", "updatedValue")
        )
        val fakeCreated2 = fakeCreatedEvent(
            uuidV7(),
            1,
            UUID.randomUUID(),
            UUID.randomUUID(),
            genericObjectMapper.createObjectNode().put("root", "definedValue")
        )

        fakeEventStore.saveAll(listOf(fakeCreated1, fakeCreated2, fakeUpdated1))
            .collectList()
            .flatMapMany { fakeEventStore.loadAllEvents(fakeCreated1.aggregateId) }
            .`as`(StepVerifier::create)
            .assertNext {  assertThat(it).isEqualTo(fakeCreated1) }
            .assertNext {  assertThat(it).isEqualTo(fakeUpdated1) }
            .verifyComplete()
    }
}