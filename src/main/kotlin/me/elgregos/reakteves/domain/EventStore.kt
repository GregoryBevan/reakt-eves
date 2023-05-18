package me.elgregos.reakteves.domain

import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

interface EventStore<E : Event<IdType>, IdType> {

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
    fun loadAllEvents(aggregateId: IdType): Flux<E>

}