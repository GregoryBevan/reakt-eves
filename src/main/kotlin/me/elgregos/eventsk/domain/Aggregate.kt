package me.elgregos.eventsk.domain

import reactor.core.publisher.Mono

interface Aggregate<E: Event<IdType>, IdType> {

    /**
     * Add new event to the aggregate after event version checking
     */
    fun applyNewEvent(event: E): Mono<E>
}