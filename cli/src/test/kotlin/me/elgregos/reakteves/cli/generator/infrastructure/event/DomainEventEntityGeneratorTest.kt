package me.elgregos.reakteves.cli.generator.infrastructure.event

import assertk.assertThat
import assertk.assertions.isEqualTo
import me.elgregos.reakteves.cli.generator.GeneratorTest
import kotlin.test.Test

internal class DomainEventEntityGeneratorTest: GeneratorTest() {

    @Test
    fun `should generate domain event entity`() {
        testTemplateEngine.render(
            "infrastructure/event/DomainEventEntity.jte",
            mapOf(domainPackageEntry, domainEntry, domainPrefixEntry, domainTableEntry),
            output
        )
        assertThat(output.toString()).isEqualTo("""
            package com.elgregos.escape.camp.game.infrastructure.event
            
            import com.fasterxml.jackson.databind.JsonNode
            import com.elgregos.escape.camp.game.domain.event.GameEvent
            import me.elgregos.reakteves.infrastructure.event.EventEntity
            import org.springframework.data.relational.core.mapping.Table
            import java.time.LocalDateTime
            import java.util.*
            
            @Table("game_event")
            data class GameEventEntity(
                @get:JvmName("id") val id: UUID,
                override val version: Int = 1,
                override val createdAt: LocalDateTime,
                override val createdBy: UUID,
                override val eventType: String,
                override val aggregateId: UUID,
                override val event: JsonNode
            ) : EventEntity<GameEvent, UUID, UUID>(
                id,
                version,
                createdAt,
                createdBy,
                eventType,
                aggregateId,
                event
            )
        """.trimIndent())
    }

}