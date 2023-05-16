package me.elgregos.eventsk.domain

import me.elgregos.eventsk.infrastructure.EventEntity
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

interface EventRepository<EE: EventEntity<E, IdType>, E: Event<IdType>, IdType> {

    fun insert(eventEntity: EE): Mono<EE>

    fun insertAll(eventEntities: List<EE>): Flux<EE>

    fun findByAggregateId(aggregateId: IdType): Flux<EE>
}