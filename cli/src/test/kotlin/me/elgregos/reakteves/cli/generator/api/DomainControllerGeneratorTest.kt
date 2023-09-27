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
            import org.springframework.http.HttpStatus
            import reactor.kotlin.core.publisher.toMono
            import java.util.*
            
            @RestController
            @RequestMapping(
                path = ["/api/game"]
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
                    gameCommandHandler.handle(GameCommand.CreateGame(createdBy = UUID.randomUUID()))
                        .toMono()
                        .map { mapOf(Pair("gameId", it.aggregateId)) }
            
            }
        """.trimIndent())
    }
}