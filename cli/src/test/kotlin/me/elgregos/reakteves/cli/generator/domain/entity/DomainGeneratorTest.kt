package me.elgregos.reakteves.cli.generator.domain.entity

import assertk.assertThat
import assertk.assertions.isEqualTo
import me.elgregos.reakteves.cli.generator.GeneratorTest
import kotlin.test.Test

internal class DomainGeneratorTest: GeneratorTest() {

    @Test
    fun `should generate domain entity`() {
        testTemplateEngine.render(
            "domain/entity/Domain.jte",
            mapOf(domainPackageEntry, domainEntry),
            output
        )
        assertThat(output.toString()).isEqualTo("""
            package com.elgregos.escape.camp.game.domain.entity

            import me.elgregos.reakteves.domain.entity.DomainEntity
            import java.time.LocalDateTime
            import java.util.*
            
            data class Game(
                override val id: UUID,
                override val version: Int = 1,
                override val createdAt: LocalDateTime,
                override val createdBy: UUID,
                override val updatedAt: LocalDateTime = createdAt,
                override val updatedBy: UUID = createdBy,
            ) : DomainEntity<UUID, UUID> {
            
            }
        """.trimIndent())
    }
}