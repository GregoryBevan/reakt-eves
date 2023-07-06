package me.elgregos.reakteves.infrastructure.event

import me.elgregos.reakteves.domain.event.Event
import org.springframework.data.r2dbc.repository.R2dbcRepository
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

interface EventEntityRepository<EE: EventEntity<E, ID, UserID>, E: Event<ID, UserID>, ID, UserID>: R2dbcRepository<EE, ID> {
    fun insert(eventEntity: EE): Mono<EE> = save(eventEntity.also { it.markNew() })

    fun insertAll(eventEntities: List<EE>) = saveAll(eventEntities.onEach { it.markNew() })

    fun findByAggregateIdOrderBySequenceNum(aggregateId: ID): Flux<EE>
}
