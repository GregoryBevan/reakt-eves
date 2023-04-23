package me.elgregos.eventsk

import io.github.oshai.KotlinLogging
import org.springframework.scheduling.annotation.Async
import reactor.core.publisher.BaseSubscriber
import reactor.core.publisher.Sinks
import reactor.core.publisher.Sinks.Many
import reactor.util.concurrent.Queues
import java.util.*
import java.util.concurrent.atomic.AtomicInteger

private val logger = KotlinLogging.logger {}

class ReactorEventBus<IdType> : EventBus<IdType> {
    private val sink: Many<Event<IdType>> = Sinks.many().multicast().onBackpressureBuffer(Queues.SMALL_BUFFER_SIZE, false)

    private val publishedCount = AtomicInteger(0)

    override fun publishEvent(event: Event<IdType>): Event<IdType> {
        sink.emitNext(event, Sinks.EmitFailureHandler.FAIL_FAST)
        logger.debug { "Published : ${publishedCount.addAndGet(1)}" }
        return event
    }

    fun subscribe(subscriber: ReactorEventSubscriber<IdType>) {
        sink.asFlux().subscribe(subscriber)
    }
}

class ReactorEventPublisher<IdType>(private val reactorEventBus: ReactorEventBus<IdType>) : EventPublisher<IdType> {

    @Async("asyncExecutor")
    override fun publish(event: Event<IdType>): Event<IdType> {
        logger.debug { "New event published : $event" }
        reactorEventBus.publishEvent(event)
        return event
    }

    fun publish(events: List<Event<IdType>>) {
        events.forEach { publish(it) }
    }

}

abstract class ReactorEventSubscriber<IdType>(private val reactorEventBus: ReactorEventBus<IdType>) : EventSubscriber<IdType>, BaseSubscriber<Event<IdType>>() {

    private val receivedCount = AtomicInteger(0)

//    @Async("asyncExecutor")
    override fun hookOnNext(event: Event<IdType>) {
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
