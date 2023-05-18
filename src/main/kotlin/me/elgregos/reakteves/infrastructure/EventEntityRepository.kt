package me.elgregos.reakteves.infrastructure

import me.elgregos.reakteves.domain.Event
import me.elgregos.reakteves.domain.EventRepository
import org.springframework.data.r2dbc.repository.R2dbcRepository
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

interface EventEntityRepository<EE: EventEntity<E, IdType>, E: Event<IdType>, IdType: Any>:
    EventRepository<EE, E, IdType>,R2dbcRepository<EE, IdType> {
    override fun insert(eventEntity: EE): Mono<EE> = save(eventEntity.also { it.markNew() })

    override fun insertAll(eventEntities: List<EE>) = saveAll(eventEntities.onEach { it.markNew() })

    override fun findByAggregateId(aggregateId: IdType): Flux<EE>
}
