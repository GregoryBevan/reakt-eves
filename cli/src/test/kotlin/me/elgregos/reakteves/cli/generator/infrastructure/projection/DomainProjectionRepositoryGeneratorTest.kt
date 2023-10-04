package me.elgregos.reakteves.cli.generator.infrastructure.projection

import assertk.assertThat
import assertk.assertions.isEqualTo
import me.elgregos.reakteves.cli.generator.GeneratorTest
import kotlin.test.Test

internal class DomainProjectionRepositoryGeneratorTest: GeneratorTest() {

    @Test
    fun `should generate domain projection repository`() {
        testTemplateEngine.render(
            "infrastructure/projection/DomainProjectionRepository.jte",
            mapOf(domainPackageEntry, domainEntry),
            output
        )
        assertThat(output.toString()).isEqualTo("""
            package com.elgregos.escape.camp.game.infrastructure.projection
            
            import com.elgregos.escape.camp.game.domain.entity.Game
            import me.elgregos.reakteves.infrastructure.projection.ProjectionRepository
            import java.util.*
            
            interface GameProjectionRepository : ProjectionRepository<GameEntity, Game, UUID, UUID>
        """.trimIndent())
    }
}