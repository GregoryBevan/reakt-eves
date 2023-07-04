package me.elgregos.reakteves.infrastructure.event

import me.elgregos.reakteves.domain.event.Event
import me.elgregos.reakteves.domain.event.EventStore
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import kotlin.reflect.KClass

class DefaultEventStore<EE: EventEntity<E, IdType>, E : Event<IdType>, IdType: Any>(
    private val eventEntityRepository: EventEntityRepository<EE, E, IdType>,
    private val eventEntityClass: KClass<EE>,
    private val eventClass: KClass<E>
) : EventStore<E, IdType> {

    override fun save(event: E): Mono<E> =
        eventEntityRepository.insert(EventEntity.fromEvent(event, eventEntityClass))
            .map { it.toEvent(eventClass) }

    override fun saveAll(events: List<E>): Flux<E> =
        events.map { EventEntity.fromEvent(it, eventEntityClass) }
            .let { eventEntityRepository.insertAll(it) }
            .map { it.toEvent(eventClass) }

    override fun loadAllEvents(aggregateId: IdType): Flux<E> =
        eventEntityRepository.findByAggregateIdOrderBySequenceNum(aggregateId)
            .map { it.toEvent(eventClass) }
}