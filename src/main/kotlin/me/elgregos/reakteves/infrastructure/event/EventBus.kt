package me.elgregos.reakteves.infrastructure.event

import me.elgregos.reakteves.domain.event.Event
import reactor.core.publisher.Mono

interface EventBus<ID, UserID> {
    fun publishEvent(event: Event<ID, UserID>): Event<ID, UserID>
}

interface EventPublisher<IdType, UserID> {
    fun publish(event: Event<IdType, UserID>): Event<IdType, UserID>
}

interface EventSubscriber<IdType, UserID> {
    fun onEvent(event: Event<IdType, UserID>): Mono<Void>

    fun subscribe()

    fun unsubscribe()
}
