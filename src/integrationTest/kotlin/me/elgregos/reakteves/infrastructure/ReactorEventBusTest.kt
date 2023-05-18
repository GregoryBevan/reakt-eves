package me.elgregos.reakteves.infrastructure

import assertk.assertThat
import assertk.assertions.isEqualTo
import me.elgregos.reakteves.config.BaseIntegrationTest
import me.elgregos.reakteves.domain.Event
import me.elgregos.reakteves.domain.fakeCreatedEvent
import me.elgregos.reakteves.libs.genericObjectMapper
import org.awaitility.kotlin.await
import org.springframework.beans.factory.annotation.Autowired
import reactor.core.publisher.Mono
import java.util.*
import java.util.concurrent.TimeUnit.SECONDS
import kotlin.test.BeforeTest
import kotlin.test.Test

internal class ReactorEventBusTest : BaseIntegrationTest() {

    @Autowired
    private lateinit var reactorEventPublisher: ReactorEventPublisher<UUID>

    @Autowired
    private lateinit var reactorEventBus: ReactorEventBus<UUID>

    private lateinit var testReactorEventSubscriber: TestReactorEventSubscriber

    @BeforeTest
    fun setUp() {
        testReactorEventSubscriber = TestReactorEventSubscriber(reactorEventBus)
    }

    @Test
    fun `should receive no event if no subscriber added`() {
        reactorEventPublisher.publish(
            fakeCreatedEvent(
                UUID.randomUUID(),
                1,
                UUID.randomUUID(),
                UUID.randomUUID(),
                genericObjectMapper.createObjectNode().put("field1", "value1")
            )
        )

        await.pollDelay(5, SECONDS)
            .until { testReactorEventSubscriber.events.isEmpty() }
    }

    @Test
    fun `should receive event if subscriber added`() {
        testReactorEventSubscriber.subscribe()
        val event =
            fakeCreatedEvent(
                UUID.randomUUID(),
                1,
                UUID.randomUUID(),
                UUID.randomUUID(),
                genericObjectMapper.createObjectNode().put("field1", "value1")
            )

        reactorEventPublisher.publish(event)

        await.pollDelay(1, SECONDS)
            .untilAsserted { assertThat(testReactorEventSubscriber.events.toList()).isEqualTo(listOf(event)) }

    }

    @Test
    fun `should receive two events if subscriber added`() {
        testReactorEventSubscriber.subscribe()
        val events =
            listOf(
                fakeCreatedEvent(
                    UUID.randomUUID(),
                    1,
                    UUID.randomUUID(),
                    UUID.randomUUID(),
                    genericObjectMapper.createObjectNode().put("field1", "value1")
                ),
                fakeCreatedEvent(
                    UUID.randomUUID(),
                    1,
                    UUID.randomUUID(),
                    UUID.randomUUID(),
                    genericObjectMapper.createObjectNode().put("field2", "value2")
                )
            )

        reactorEventPublisher.publish(events)

        await.pollDelay(1, SECONDS)
            .untilAsserted {
                assertThat(testReactorEventSubscriber.events.toList()).isEqualTo(
                    listOf(
                        events[0],
                        events[1]
                    )
                )
            }
    }
}

internal class TestReactorEventSubscriber(reactorEventBus: ReactorEventBus<UUID>) :
    ReactorEventSubscriber<UUID>(reactorEventBus) {

    var events = mutableListOf<Event<UUID>>()

    override fun onEvent(event: Event<UUID>) = Mono.just(event)
        .doOnNext{ events.add(it) }
        .then()

}