package me.elgregos.reakteves.infrastructure.projection

import me.elgregos.reakteves.domain.entity.DomainEntity
import org.springframework.data.r2dbc.repository.R2dbcRepository
import reactor.core.publisher.Mono

interface ProjectionRepository<PE: ProjectionEntity<DE, ID, UserID>, DE: DomainEntity<ID, UserID>, ID, UserID>: R2dbcRepository<PE, ID> {

    fun insert(projectionEntity: PE): Mono<PE> =
        save(projectionEntity.apply(ProjectionEntity<DE, ID, UserID>::markNew))


}