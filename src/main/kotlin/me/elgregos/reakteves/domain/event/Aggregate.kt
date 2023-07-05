package me.elgregos.reakteves.domain.event

import reactor.core.publisher.Mono

interface Aggregate<E: Event<ID, UserID>, ID, UserID> {

    /**
     * Add new event to the aggregate after event version checking
     */
    fun applyNewEvent(event: E): Mono<E>
}