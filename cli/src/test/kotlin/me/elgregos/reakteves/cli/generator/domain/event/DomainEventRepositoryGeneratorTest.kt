package me.elgregos.reakteves.cli.generator.domain.event

import assertk.assertThat
import assertk.assertions.isEqualTo
import me.elgregos.reakteves.cli.generator.GeneratorTest
import kotlin.test.Test

internal class DomainEventRepositoryGeneratorTest : GeneratorTest() {

    @Test
    fun `should generate domain event repository`() {
        testTemplateEngine.render(
            "domain/event/DomainEventRepository.jte",
            mapOf(domainPackageEntry, domainEntry),
            output
        )
        assertThat(output.toString()).isEqualTo(
            """
            package com.elgregos.escape.camp.game.domain.event

            import com.elgregos.escape.camp.game.infrastructure.event.GameEventEntity
            import me.elgregos.reakteves.infrastructure.event.EventEntityRepository
            import java.util.*
            
            interface GameEventRepository : EventEntityRepository<GameEventEntity, GameEvent, UUID, UUID>
        """.trimIndent()
        )
    }
}