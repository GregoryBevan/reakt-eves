package me.elgregos.eventsk.infrastructure

import me.elgregos.eventsk.domain.Event
import reactor.core.publisher.Mono

interface EventBus<IdType> {
    fun publishEvent(event: Event<IdType>): Event<IdType>
}

interface EventPublisher<IdType> {
    fun publish(event: Event<IdType>): Event<IdType>
}

interface EventSubscriber<IdType> {
    fun onEvent(event: Event<IdType>): Mono<Void>

    fun subscribe()

    fun unsubscribe()
}
