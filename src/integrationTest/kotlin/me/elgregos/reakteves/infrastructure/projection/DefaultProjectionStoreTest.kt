package me.elgregos.reakteves.infrastructure.projection

import assertk.assertThat
import assertk.assertions.isEqualTo
import me.elgregos.reakteves.config.BaseIntegrationTest
import me.elgregos.reakteves.domain.entity.FakeDomainEntity
import me.elgregos.reakteves.domain.projection.ProjectionStore
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import reactor.test.StepVerifier
import java.util.*

internal class DefaultProjectionStoreTest: BaseIntegrationTest() {

    @Autowired
    private lateinit var fakeProjectionStore: ProjectionStore<FakeDomainEntity, UUID, UUID>

    @Test
    fun `should save a projection entity`() {
        val domainEntity = FakeDomainEntity()
        fakeProjectionStore.insert(domainEntity)
            .`as`(StepVerifier::create)
            .assertNext{ assertThat(it).isEqualTo(domainEntity) }
            .verifyComplete()
    }

    @Test
    fun `should update a projection entity`() {
        val domainEntity = FakeDomainEntity()
        val updatedDomainEntity = domainEntity.copy(version = 2)
        fakeProjectionStore.insert(domainEntity)
            .flatMap { fakeProjectionStore.update(updatedDomainEntity) }
            .`as`(StepVerifier::create)
            .assertNext{ assertThat(it).isEqualTo(updatedDomainEntity) }
            .verifyComplete()
    }

    @Test
    fun `should find a projection entity`() {
        val domainEntity = FakeDomainEntity()
        fakeProjectionStore.insert(domainEntity)
            .flatMap { fakeProjectionStore.find(domainEntity.id) }
            .`as`(StepVerifier::create)
            .assertNext{ assertThat(it).isEqualTo(domainEntity) }
            .verifyComplete()
    }

    @Test
    fun `should find list of projection entities`() {
        val firstDomainEntity = FakeDomainEntity(id = UUID.randomUUID())
        val secondDomainEntity = FakeDomainEntity(id = UUID.randomUUID())
        val thirdDomainEntity = FakeDomainEntity(id = UUID.randomUUID())
        fakeProjectionStore.insert(firstDomainEntity)
            .flatMap { fakeProjectionStore.insert(secondDomainEntity) }
            .flatMap { fakeProjectionStore.insert(thirdDomainEntity) }
            .flatMapMany { fakeProjectionStore.list() }
            .`as`(StepVerifier::create)
            .assertNext{ assertThat(it).isEqualTo(firstDomainEntity) }
            .assertNext{ assertThat(it).isEqualTo(secondDomainEntity) }
            .assertNext{ assertThat(it).isEqualTo(thirdDomainEntity) }
            .verifyComplete()
    }

}