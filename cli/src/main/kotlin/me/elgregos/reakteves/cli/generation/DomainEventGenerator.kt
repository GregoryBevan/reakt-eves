package me.elgregos.reakteves.cli.generation

import gg.jte.ContentType
import gg.jte.TemplateEngine
import gg.jte.output.FileOutput
import java.io.File
import java.nio.file.Path


fun generateDomainEvent(kotlinSourcePath: Path, domain: String, domainPackage: String) {
    val templateEngine = TemplateEngine.createPrecompiled(ContentType.Plain)
//    val output = StringOutput()
    val output = FileOutput(kotlinSourcePath.resolve(domainPackage.replace(".", File.separator)).resolve("domain").resolve("event").resolve("${domain}Event.kt"))
    templateEngine.render("domain/event/DomainEvent.jte", mapOf("domainPackage" to domainPackage, "domain" to domain), output)
    output.close()
}