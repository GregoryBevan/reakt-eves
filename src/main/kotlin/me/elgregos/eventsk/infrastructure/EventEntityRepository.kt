package me.elgregos.eventsk.infrastructure

import me.elgregos.eventsk.domain.Event
import org.springframework.data.r2dbc.repository.R2dbcRepository

interface EventEntityRepository<EE: EventEntity<E, IdType>, E: Event<IdType>, IdType>: R2dbcRepository<EE, IdType>

fun <T: EventEntityRepository<EE, E, IdType>, EE: EventEntity<E, IdType>, E: Event<IdType>, IdType> T.insert(eventEntity: EE) =
    save(eventEntity.also { it.markNew() })

fun <T: EventEntityRepository<EE, E, IdType>, EE: EventEntity<E, IdType>, E: Event<IdType>, IdType> T.insertAll(eventEntities: List<EE>) =
    saveAll(eventEntities.onEach { it.markNew() })
