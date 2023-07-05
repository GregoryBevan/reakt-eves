package me.elgregos.reakteves.infrastructure.projection

import assertk.assertThat
import assertk.assertions.isEqualTo
import me.elgregos.reakteves.domain.entity.FakeDomainEntity
import me.elgregos.reakteves.domain.entity.fakeDomainEntityCreatedAt
import me.elgregos.reakteves.domain.entity.fakeDomainEntityJson
import me.elgregos.reakteves.infrastructure.projection.ProjectionEntity.Companion.fromDomainEntity
import java.util.*
import kotlin.test.Test

class ProjectionEntityTest {

    @Test
    fun `should convert domain entity to projection entity`() {
        val fakeDomainEntity = FakeDomainEntity()
        assertThat(fromDomainEntity(fakeDomainEntity, FakeProjectionEntity::class)).isEqualTo(fakeProjectionEntity)
    }

    @Test
    fun `should convert projection entity to domain entity`() {
        val fakeProjectionEntity = FakeProjectionEntity(UUID.randomUUID(), version = 1, createdAt = fakeDomainEntityCreatedAt, updatedAt = fakeDomainEntityCreatedAt, details = fakeDomainEntityJson)
        assertThat(fakeProjectionEntity.toDomainEntity(FakeDomainEntity::class)).isEqualTo(FakeDomainEntity())
    }
}