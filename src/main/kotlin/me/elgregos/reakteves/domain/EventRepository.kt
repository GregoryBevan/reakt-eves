package me.elgregos.reakteves.domain

import me.elgregos.reakteves.infrastructure.EventEntity
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

interface EventRepository<EE: EventEntity<E, IdType>, E: Event<IdType>, IdType> {

    fun insert(eventEntity: EE): Mono<EE>

    fun insertAll(eventEntities: List<EE>): Flux<EE>

    fun findByAggregateIdOrderBySequenceNum(aggregateId: IdType): Flux<EE>
}