package me.elgregos.reakteves.domain.projection

import me.elgregos.reakteves.domain.entity.DomainEntity
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

interface ProjectionStore<DE: DomainEntity<in ID, UserID>, in ID, UserID> {

    fun list(): Flux<DE>

    fun find(domainEntityId: ID): Mono<DE>

    fun insert(domainEntity: DE): Mono<DE>

    fun update(domainEntity: DE): Mono<DE>
}