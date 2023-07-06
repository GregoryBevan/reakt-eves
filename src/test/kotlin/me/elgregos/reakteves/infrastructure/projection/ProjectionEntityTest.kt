package me.elgregos.reakteves.infrastructure.projection

import assertk.assertThat
import assertk.assertions.isEqualTo
import me.elgregos.reakteves.domain.entity.FakeDomainEntity
import me.elgregos.reakteves.infrastructure.projection.ProjectionEntity.Companion.fromDomainEntity
import kotlin.test.Test

class ProjectionEntityTest {

    @Test
    fun `should convert domain entity to projection entity`() {
        assertThat(fromDomainEntity(FakeDomainEntity(), FakeProjectionEntity::class)).isEqualTo(fakeProjectionEntity)
    }

    @Test
    fun `should convert projection entity to domain entity`() {
        assertThat(fakeProjectionEntity.toDomainEntity(FakeDomainEntity::class)).isEqualTo(FakeDomainEntity())
    }
}