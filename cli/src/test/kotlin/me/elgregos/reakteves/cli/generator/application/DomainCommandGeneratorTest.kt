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

            import me.elgregos.reakteves.application.Command
            import me.elgregos.reakteves.libs.nowUTC
            import java.time.LocalDateTime
            import java.util.*
            
            sealed class GameCommand(open val gameId: UUID) : Command {
            
                data class CreateGame(
                    override val gameId: UUID = UUID.randomUUID(),
                    val createdBy: UUID,
                    val createdAt: LocalDateTime = nowUTC()
                ) : GameCommand(gameId)
            
            }
        """.trimIndent())
    }
}