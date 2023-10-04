package me.elgregos.reakteves.cli.generator.infrastructure.projection

import assertk.assertThat
import assertk.assertions.isEqualTo
import me.elgregos.reakteves.cli.generator.GeneratorTest
import kotlin.test.Test

internal class DomainEntityGeneratorTest: GeneratorTest() {

    @Test
    fun `should generate domain entity`() {
        testTemplateEngine.render(
            "infrastructure/projection/DomainEntity.jte",
            mapOf(domainPackageEntry, domainEntry, domainTableEntry),
            output
        )
        assertThat(output.toString()).isEqualTo("""
            package com.elgregos.escape.camp.game.infrastructure.projection
            
            import com.fasterxml.jackson.databind.JsonNode
            import com.elgregos.escape.camp.game.domain.entity.Game
            import me.elgregos.reakteves.infrastructure.projection.ProjectionEntity
            import org.springframework.data.relational.core.mapping.Table
            import java.time.LocalDateTime
            import java.util.*
            
            @Table("game")
            data class GameEntity(
                @get:JvmName("id") val id: UUID,
                override val sequenceNum: Long? = null,
                override val version: Int,
                override val createdAt: LocalDateTime,
                override val createdBy: UUID,
                override val updatedAt: LocalDateTime,
                override val updatedBy: UUID,
                override val details: JsonNode
            ) : ProjectionEntity<Game, UUID, UUID>(id, sequenceNum, version, createdAt, createdBy, updatedAt, updatedBy, details)
        """.trimIndent())
    }
}