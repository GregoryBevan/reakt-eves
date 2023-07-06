package me.elgregos.reakteves.infrastructure.projection

import me.elgregos.reakteves.domain.entity.FakeDomainEntity
import java.util.UUID

interface FakeProjectionRepository: ProjectionRepository<FakeProjectionEntity, FakeDomainEntity, UUID, UUID>