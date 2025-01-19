package me.elgregos.reakteves.infrastructure.projection

import me.elgregos.reakteves.domain.entity.DomainEntity
import me.elgregos.reakteves.domain.projection.ProjectionStore
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import kotlin.reflect.KClass

class DefaultProjectionStore<PE : ProjectionEntity<DE, ID, UserID>, DE : DomainEntity<ID, UserID>, ID : Any, UserID>(
    private val projectionRepository: ProjectionRepository<PE, DE, ID, UserID>,
    private val projectionEntityClass: KClass<PE>,
    private val domainEntityClass: KClass<DE>
) : ProjectionStore<DE, ID, UserID> {

    override fun list(): Flux<DE> =
        projectionRepository.findAllByOrderByIdAsc()
            .map { it.toDomainEntity(domainEntityClass) }

    override fun find(domainEntityId: ID): Mono<DE> =
        projectionRepository.findById(domainEntityId)
            .map { it.toDomainEntity(domainEntityClass) }

    override fun insert(domainEntity: DE): Mono<DE> =
        projectionRepository.insert(ProjectionEntity.fromDomainEntity(
            domainEntity,
            projectionEntityClass
        ))
            .map { it.toDomainEntity(domainEntityClass) }

    override fun update(domainEntity: DE): Mono<DE> =
        projectionRepository.findById(domainEntity.id)
            .flatMap { projectionRepository.save(ProjectionEntity.fromDomainEntity(domainEntity, projectionEntityClass)) }
            .map { it.toDomainEntity(domainEntityClass) }

}