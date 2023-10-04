package me.elgregos.reakteves.cli.generator.infrastructure.event

import assertk.assertThat
import assertk.assertions.isEqualTo
import me.elgregos.reakteves.cli.generator.GeneratorTest
import kotlin.test.Test

internal class DomainEventEntityRepositoryGeneratorTest: GeneratorTest() {

    @Test
    fun `should generate domain event entity repository`() {
        testTemplateEngine.render(
            "infrastructure/event/DomainEventEntityRepository.jte",
            mapOf(domainPackageEntry, domainEntry),
            output
        )
        assertThat(output.toString()).isEqualTo("""
            package com.elgregos.escape.camp.game.infrastructure.event
            
            import com.elgregos.escape.camp.game.domain.event.GameEvent
            import com.elgregos.escape.camp.game.domain.event.GameEventRepository
            import me.elgregos.reakteves.infrastructure.event.EventEntityRepository
            import java.util.*
            
            interface GameEventEntityRepository : EventEntityRepository<GameEventEntity, GameEvent, UUID, UUID>, GameEventRepository
        """.trimIndent())
    }
}