package me.elgregos.reakteves.cli.generator.infrastructure.projection

import assertk.assertThat
import assertk.assertions.isEqualTo
import me.elgregos.reakteves.cli.generator.GeneratorTest
import kotlin.test.Test

internal class DomainProjectionSubscriberGeneratorTest: GeneratorTest() {

    @Test
    fun `should generate domain projection subscriber`() {
        testTemplateEngine.render(
            "infrastructure/projection/DomainProjectionSubscriber.jte",
            mapOf(domainPackageEntry, domainEntry, domainPrefixEntry),
            output
        )
        assertThat(output.toString()).isEqualTo("""
            package com.elgregos.escape.camp.game.infrastructure.projection
            
            import io.github.oshai.kotlinlogging.KotlinLogging
            import jakarta.annotation.PostConstruct
            import com.elgregos.escape.camp.game.domain.entity.Game
            import com.elgregos.escape.camp.game.domain.event.GameEvent
            import com.elgregos.escape.camp.game.domain.event.GameEvent.*
            import me.elgregos.reakteves.domain.JsonConvertible.Companion.fromJson
            import me.elgregos.reakteves.domain.event.Event
            import me.elgregos.reakteves.domain.projection.ProjectionStore
            import me.elgregos.reakteves.infrastructure.event.ReactorEventBus
            import me.elgregos.reakteves.infrastructure.event.ReactorEventSubscriber
            import org.springframework.stereotype.Component
            import reactor.core.publisher.Mono
            import java.util.*
            
            private val logger = KotlinLogging.logger {}
            
            @Component
            class GameProjectionSubscriber(
                reactorEventBus: ReactorEventBus<UUID, UUID>,
                private val gameProjectionStore: ProjectionStore<Game, UUID, UUID>,
            ) : ReactorEventSubscriber<UUID, UUID>(reactorEventBus) {
            
                @PostConstruct
                fun initialize() {
                    subscribe()
                }
            
                override fun onEvent(event: Event<UUID, UUID>): Mono<Void> {
                    return Mono.just(event)
                        .filter { e -> e is GameEvent }
                        .cast(GameEvent::class.java)
                        .flatMap {
                            when (it) {
                                is GameCreated -> createGame(it)
                            }
                        }
                        .doOnError { error -> logger.error(error) { "An error occurred while processing event" } }
                        .then()
                }
            
                private fun createGame(event: GameCreated) =
                    gameProjectionStore.insert(fromJson(event.event, Game::class.java))
            }
        """.trimIndent())
    }
}