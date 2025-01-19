package me.elgregos.reakteves.cli.generator.application

import assertk.assertThat
import assertk.assertions.isEqualTo
import me.elgregos.reakteves.cli.generator.GeneratorTest
import org.junit.jupiter.api.Test

internal class DomainCommandGeneratorTest: GeneratorTest() {

    @Test
    fun `should generate domain command`() {
        testTemplateEngine.render(
            "application/DomainCommand.jte",
            mapOf(domainPackageEntry, domainEntry, domainPrefixEntry),
            output
        )
        assertThat(output.toString()).isEqualTo("""
            package com.elgregos.escape.camp.game.application

            import com.elgregos.escape.camp.game.domain.entity.Game
            import me.elgregos.reakteves.application.Command
            import java.util.*
            
            sealed class GameCommand(open val gameId: UUID) : Command {
            
                data class CreateGame(
                    val game: Game
                ) : GameCommand(gameId = game.id)
            
            }
        """.trimIndent())
    }
}