package me.elgregos.eventsk

import me.elgregos.eventsk.infrastructure.ReactorEventBus
import me.elgregos.eventsk.infrastructure.ReactorEventPublisher
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
@EnableR2dbcRepositories(basePackages = ["me.elgregos.eventsk"])
class TestConfig {

	@Bean
	fun reactorEventBus() = ReactorEventBus<UUID>()

	@Bean
	fun reactorEventPublisher(reactorEventBus: ReactorEventBus<UUID>) = ReactorEventPublisher(reactorEventBus)
}