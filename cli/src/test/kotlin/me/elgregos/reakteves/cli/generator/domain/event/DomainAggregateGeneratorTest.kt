package me.elgregos.reakteves.cli.generator.domain.event

import assertk.assertThat
import assertk.assertions.isEqualTo
import me.elgregos.reakteves.cli.generator.GeneratorTest
import kotlin.test.Test

internal class DomainAggregateGeneratorTest: GeneratorTest() {

    @Test
    fun `should generate domain aggregate`() {
        testTemplateEngine.render(
            "domain/event/DomainAggregate.jte",
            mapOf(domainPackageEntry, domainEntry, domainPrefixEntry),
            output
        )
        assertThat(output.toString()).isEqualTo("""
            package com.elgregos.escape.camp.game.domain.event
            
            import com.elgregos.escape.camp.game.domain.entity.Game
            import com.elgregos.escape.camp.game.domain.event.GameEvent.*
            import me.elgregos.reakteves.domain.event.EventStore
            import me.elgregos.reakteves.domain.event.JsonAggregate
            import reactor.core.publisher.Flux
            import java.util.*
            
            class GameAggregate(
                gameId: UUID, 
                gameEventStore: EventStore<GameEvent, UUID, UUID>
            ) : JsonAggregate<GameEvent, UUID, UUID>(gameId, gameEventStore) {
            
                fun createGame(game: Game): Flux<GameEvent> =
                    Flux.just(GameCreated(game)) 
            }
        """.trimIndent())
    }
}