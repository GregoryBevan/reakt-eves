package me.elgregos.reakteves.cli.generator.application

import assertk.assertThat
import assertk.assertions.isEqualTo
import me.elgregos.reakteves.cli.generator.GeneratorTest
import org.junit.jupiter.api.Test

internal class DomainCommandHandlerGeneratorTest: GeneratorTest() {

    @Test
    fun `should generate domain command handler`() {
        testTemplateEngine.render(
            "application/DomainCommandHandler.jte",
            mapOf(domainPackageEntry, domainEntry, domainPrefixEntry),
            output
        )
        assertThat(output.toString()).isEqualTo("""
            package com.elgregos.escape.camp.game.application

            import com.elgregos.escape.camp.game.application.GameCommand.CreateGame
            import com.elgregos.escape.camp.game.domain.event.GameAggregate
            import com.elgregos.escape.camp.game.domain.event.GameEvent
            import me.elgregos.reakteves.domain.event.EventStore
            import me.elgregos.reakteves.infrastructure.event.ReactorEventPublisher
            import org.springframework.stereotype.Service
            import java.util.*
            
            @Service
            class GameCommandHandler(
                val gameEventStore: EventStore<GameEvent, UUID, UUID>,
                val gameEventPublisher: ReactorEventPublisher<UUID, UUID>
            ) {
            
                fun handle(gameCommand: GameCommand) =
                    when (gameCommand) {
                        is CreateGame -> createGame(gameCommand)
                    }
                        .flatMap { gameEventStore.save(it) }
                        .doOnNext { gameEventPublisher.publish(it) }

                private fun createGame(gameCommand: CreateGame) =
                    GameAggregate(gameCommand.gameId, gameEventStore)
                        .createGame(gameCommand.game)
            
            }
        """.trimIndent())
    }
}