package me.elgregos.reakteves

import me.elgregos.reakteves.domain.event.EventStore
import me.elgregos.reakteves.domain.FakeEvent
import me.elgregos.reakteves.infrastructure.*
import me.elgregos.reakteves.infrastructure.event.DefaultEventStore
import me.elgregos.reakteves.infrastructure.event.ReactorEventBus
import me.elgregos.reakteves.infrastructure.event.ReactorEventPublisher
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.data.r2dbc.repository.config.EnableR2dbcRepositories
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
    fun reactorEventBus() = ReactorEventBus<UUID>()

    @Bean
    fun reactorEventPublisher(reactorEventBus: ReactorEventBus<UUID>) = ReactorEventPublisher(reactorEventBus)

    @Bean
    fun fakeEventStore(fakeEventRepository: FakeEventRepository): EventStore<FakeEvent, UUID> =
        DefaultEventStore(fakeEventRepository, FakeEventEntity::class, FakeEvent::class)
}