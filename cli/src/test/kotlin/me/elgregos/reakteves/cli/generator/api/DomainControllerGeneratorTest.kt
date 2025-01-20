package me.elgregos.reakteves.cli.generator.api

import assertk.assertThat
import assertk.assertions.isEqualTo
import me.elgregos.reakteves.cli.generator.GeneratorTest
import org.junit.jupiter.api.Test

internal class DomainControllerGeneratorTest: GeneratorTest() {

    @Test
    fun `should generate domain controller`() {
        testTemplateEngine.render(
            "api/DomainController.jte",
            mapOf(domainPackageEntry, domainEntry, domainPathEntry, domainPrefixEntry),
            output
        )
        assertThat(output.toString()).isEqualTo("""
            package com.elgregos.escape.camp.game.api
            
            import jakarta.validation.Valid
            import org.springframework.web.bind.annotation.*
            import com.elgregos.escape.camp.game.application.GameCommand
            import com.elgregos.escape.camp.game.application.GameCommandHandler
            import com.elgregos.escape.camp.game.application.GameProjectionService
            import com.elgregos.escape.camp.game.domain.entity.Game
            import me.elgregos.reakteves.libs.nowUTC
            import me.elgregos.reakteves.libs.uuidV5
            import me.elgregos.reakteves.libs.uuidV7
            import org.springframework.http.HttpStatus
            import reactor.kotlin.core.publisher.toMono
            import java.util.*
            
            @RestController
            @RequestMapping(
                path = ["/api/games"]
            )
            class GameController(
                private val gameCommandHandler: GameCommandHandler,
                private val gameProjectionService: GameProjectionService
            ) {
            
                @GetMapping
                @ResponseStatus(HttpStatus.OK)
                fun games() = gameProjectionService.games()
            
                @GetMapping("{gameId}")
                @ResponseStatus(HttpStatus.OK)
                fun game(@PathVariable @Valid gameId: UUID) = gameProjectionService.game(gameId)
            
                @PostMapping
                @ResponseStatus(HttpStatus.CREATED)
                fun createGame() =
                    gameCommandHandler.handle(GameCommand.CreateGame(Game(id = uuidV7(), createdAt = nowUTC(), createdBy = uuidV5("creator"))))
                        .toMono()
                        .map { mapOf(Pair("gameId", it.aggregateId)) }
            
            }
        """.trimIndent())
    }
}