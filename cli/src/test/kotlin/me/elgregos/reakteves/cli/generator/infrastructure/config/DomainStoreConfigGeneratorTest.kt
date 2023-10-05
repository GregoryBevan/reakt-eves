package me.elgregos.reakteves.cli.generator.infrastructure.config

import assertk.assertThat
import assertk.assertions.isEqualTo
import me.elgregos.reakteves.cli.generator.GeneratorTest
import kotlin.test.Test

internal class DomainStoreConfigGeneratorTest: GeneratorTest() {

    @Test
    fun `should generate domain store config`() {
        testTemplateEngine.render(
            "infrastructure/config/DomainStoreConfig.jte",
            mapOf(domainPackageEntry, domainEntry, domainPrefixEntry),
            output
        )
        assertThat(output.toString()).isEqualTo("""
            package com.elgregos.escape.camp.game.infrastructure.config
            
            import com.elgregos.escape.camp.game.domain.entity.Game
            import com.elgregos.escape.camp.game.domain.event.GameEvent
            import com.elgregos.escape.camp.game.domain.event.GameEventRepository
            import com.elgregos.escape.camp.game.infrastructure.event.GameEventEntity
            import com.elgregos.escape.camp.game.infrastructure.projection.GameEntity
            import com.elgregos.escape.camp.game.infrastructure.projection.GameProjectionRepository
            import me.elgregos.reakteves.domain.event.EventStore
            import me.elgregos.reakteves.domain.projection.ProjectionStore
            import me.elgregos.reakteves.infrastructure.event.DefaultEventStore
            import me.elgregos.reakteves.infrastructure.projection.DefaultProjectionStore
            import org.springframework.context.annotation.Bean
            import org.springframework.context.annotation.Configuration
            import java.util.UUID
            
            @Configuration
            class GameStoreConfig {
            
                @Bean
                fun gameEventStore(gameEventRepository: GameEventRepository): EventStore<GameEvent, UUID, UUID> =
                    DefaultEventStore(gameEventRepository, GameEventEntity::class, GameEvent::class)
            
                @Bean
                fun gameProjectionStore(gameProjectionRepository: GameProjectionRepository): ProjectionStore<Game, UUID, UUID> =
                    DefaultProjectionStore(gameProjectionRepository, GameEntity::class, Game::class)
            }
        """.trimIndent())
    }
}