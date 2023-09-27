package me.elgregos.reakteves.infrastructure.event

import io.github.oshai.kotlinlogging.KotlinLogging
import me.elgregos.reakteves.domain.event.Event
import org.springframework.scheduling.annotation.Async
import reactor.core.publisher.BaseSubscriber
import reactor.core.publisher.Sinks
import reactor.core.publisher.Sinks.Many
import reactor.util.concurrent.Queues
import java.util.concurrent.atomic.AtomicInteger

private val logger = KotlinLogging.logger {}

class ReactorEventBus<ID, UserID> : EventBus<ID, UserID> {
    private val sink: Many<Event<ID, UserID>> = Sinks.many().multicast().onBackpressureBuffer(Queues.SMALL_BUFFER_SIZE, false)

    private val publishedCount = AtomicInteger(0)

    override fun publishEvent(event: Event<ID, UserID>): Event<ID, UserID> {
        sink.emitNext(event, Sinks.EmitFailureHandler.FAIL_FAST)
        logger.debug { "Published : ${publishedCount.addAndGet(1)}" }
        return event
    }

    fun subscribe(subscriber: ReactorEventSubscriber<ID, UserID>) {
        sink.asFlux().subscribe(subscriber)
    }
}

open class ReactorEventPublisher<IdType, UserID>(private val reactorEventBus: ReactorEventBus<IdType, UserID>) : EventPublisher<IdType, UserID> {

    @Async("asyncExecutor")
    override fun publish(event: Event<IdType, UserID>): Event<IdType, UserID> {
        logger.debug { "New event published : $event" }
        reactorEventBus.publishEvent(event)
        return event
    }

    fun publish(events: List<Event<IdType, UserID>>) {
        events.forEach { publish(it) }
    }

}

abstract class ReactorEventSubscriber<IdType, UserID>(private val reactorEventBus: ReactorEventBus<IdType, UserID>) : EventSubscriber<IdType, UserID>, BaseSubscriber<Event<IdType, UserID>>() {

    private val receivedCount = AtomicInteger(0)

//    @Async("asyncExecutor")
    override fun hookOnNext(event: Event<IdType, UserID>) {
        logger.debug {
            """
            |New event received : $event" }
            |Received : ${receivedCount.addAndGet(1)}""".trimMargin()
        }
        onEvent(event)
            .subscribe()
    }

    override fun subscribe() {
        logger.debug { "${this.javaClass.simpleName} listens to events" }
        reactorEventBus.subscribe(this)
    }

    override fun unsubscribe() {
        logger.debug { "${this.javaClass.simpleName} disposed" }
        this.dispose()
    }

}
