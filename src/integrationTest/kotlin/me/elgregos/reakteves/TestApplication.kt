package me.elgregos.reakteves

import me.elgregos.reakteves.domain.entity.FakeDomainEntity
import me.elgregos.reakteves.domain.event.EventStore
import me.elgregos.reakteves.domain.event.FakeEvent
import me.elgregos.reakteves.infrastructure.event.*
import me.elgregos.reakteves.infrastructure.projection.DefaultProjectionStore
import me.elgregos.reakteves.infrastructure.projection.FakeProjectionEntity
import me.elgregos.reakteves.infrastructure.projection.FakeProjectionRepository
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.core.io.ClassPathResource
import org.springframework.data.r2dbc.repository.config.EnableR2dbcRepositories
import org.springframework.r2dbc.connection.init.ResourceDatabasePopulator
import java.util.*

@SpringBootApplication
class TestApplication

fun main(args: Array<String>) {
    runApplication<TestApplication>(*args)
}

@TestConfiguration
@EnableR2dbcRepositories(basePackages = ["me.elgregos.reakteves"])
class TestConfig {

    @Bean
    fun fakeEventStore(fakeEventRepository: FakeEventRepository): EventStore<FakeEvent, UUID, UUID> =
        DefaultEventStore(fakeEventRepository, FakeEventEntity::class, FakeEvent::class)

    @Bean
    fun fakeProjectionStore(fakeProjectionRepository: FakeProjectionRepository) =
        DefaultProjectionStore(fakeProjectionRepository, FakeProjectionEntity::class, FakeDomainEntity::class)

    @Bean
    fun cleaner(): ResourceDatabasePopulator? {
        return ResourceDatabasePopulator(ClassPathResource("db-changelog/cleaner.sql"))
    }
}