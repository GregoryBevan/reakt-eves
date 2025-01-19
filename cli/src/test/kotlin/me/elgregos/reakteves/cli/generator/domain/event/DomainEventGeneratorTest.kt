package me.elgregos.reakteves.cli.generator.domain.event

import assertk.assertThat
import assertk.assertions.isEqualTo
import me.elgregos.reakteves.cli.generator.GeneratorTest
import kotlin.test.Test

internal class DomainEventGeneratorTest: GeneratorTest() {

    @Test
    fun `should generate domain event`() {
        testTemplateEngine.render(
            "domain/event/DomainEvent.jte",
            mapOf(domainPackageEntry, domainEntry, domainPrefixEntry),
            output
        )
        assertThat(output.toString()).isEqualTo("""
            package com.elgregos.escape.camp.game.domain.event

            import com.fasterxml.jackson.databind.JsonNode
            import com.elgregos.escape.camp.game.domain.entity.Game
            import me.elgregos.reakteves.domain.event.Event
            import me.elgregos.reakteves.libs.genericObjectMapper
            import me.elgregos.reakteves.libs.nowUTC
            import me.elgregos.reakteves.libs.uuidV7
            import java.time.LocalDateTime
            import java.util.*

            sealed class GameEvent(
                id: UUID = uuidV7(),
                version: Int,
                createdAt: LocalDateTime,
                createdBy: UUID,
                aggregateId: UUID,
                eventType: String,
                event: JsonNode
            ) : Event<UUID, UUID>(
                id, version, createdAt, createdBy, eventType, aggregateId, event
            ) {

                data class GameCreated(
                    override val version: Int = 1,
                    override val createdAt: LocalDateTime = nowUTC(),
                    override val createdBy: UUID,
                    val gameId: UUID,
                    override val event: JsonNode
                ) : GameEvent(
                    version = version,
                    createdAt = createdAt,
                    createdBy = createdBy,
                    aggregateId = gameId,
                    eventType = gameCreated::class.simpleName!!,
                    event = event
                ) {
                    constructor(game: Game) : this(
                        gameId = game.id,
                        createdAt = game.createdAt,
                        createdBy = game.createdBy,
                        event = genericObjectMapper.readTree(genericObjectMapper.writeValueAsString(game))
                    )
                }
            }
        """.trimIndent())
    }
}