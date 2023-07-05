package me.elgregos.reakteves.domain.event

import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

interface EventStore<E : Event<ID, UserID>, ID, UserID> {

    /**
     * Save the given event in event store
     */
    fun save(event: E): Mono<E>

    /**
     * Save the given event in event store
     */
    fun saveAll(events: List<E>): Flux<E>

    /**
     * Load all events from event store for the given aggregate Id
     */
    fun loadAllEvents(aggregateId: ID): Flux<E>

}