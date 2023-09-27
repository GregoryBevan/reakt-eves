package me.elgregos.reakteves.cli.generator.application

import assertk.assertThat
import assertk.assertions.isEqualTo
import me.elgregos.reakteves.cli.generator.GeneratorTest
import org.junit.jupiter.api.Test

internal class DomainProjectionServiceTest: GeneratorTest() {

    @Test
    fun `should generate domain projection service`() {
        testTemplateEngine.render(
            "application/DomainProjectionService.jte",
            mapOf(domainPackageEntry, domainEntry, domainPrefixEntry),
            output
        )
        assertThat(output.toString()).isEqualTo("""
            package com.elgregos.escape.camp.game.application
            
            import com.elgregos.escape.camp.game.domain.entity.Game
            import me.elgregos.reakteves.domain.projection.ProjectionStore
            import org.springframework.stereotype.Service
            import java.util.*
            
            @Service
            class GameProjectionService(
                private val gameProjectionStore: ProjectionStore<Game, UUID, UUID>) {
            
                fun games() =
                    gameProjectionStore.list()
            
                fun game(gameId: UUID) =
                    gameProjectionStore.find(gameId)
            }
        """.trimIndent())
    }
}