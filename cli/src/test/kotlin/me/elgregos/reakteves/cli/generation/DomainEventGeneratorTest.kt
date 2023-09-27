package me.elgregos.reakteves.cli.generation

import gg.jte.ContentType
import gg.jte.TemplateEngine
import gg.jte.output.StringOutput
import gg.jte.resolve.DirectoryCodeResolver
import java.nio.file.Paths
import kotlin.test.Test

internal class DomainEventGeneratorTest {

    @Test
    fun `should generate domain event`() {
        val codeResolver = DirectoryCodeResolver(Paths.get("src/main/jte"))

        val templateEngine = TemplateEngine.create(codeResolver, ContentType.Plain)

        val output = StringOutput()
        templateEngine.render(
            "domain/event/DomainEvent.jte",
            mapOf("domainPackage" to "com.elgregos.escape.camp.game", "domain" to "Game"),
            output
        )
        println(output)
    }
}