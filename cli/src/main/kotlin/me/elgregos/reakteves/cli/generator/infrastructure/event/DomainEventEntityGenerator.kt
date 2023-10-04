package me.elgregos.reakteves.cli.generator.infrastructure.event

import gg.jte.output.FileOutput
import me.elgregos.reakteves.cli.generator.PrecompiledTemplateEngine
import java.io.File
import java.nio.file.Path

fun generateDomainEventEntity(kotlinSourcePath: Path, domain: String, domainPackage: String, templateParams: Map<String, String>) {
    val output = FileOutput(kotlinSourcePath.resolve(domainPackage.replace(".", File.separator)).resolve("infrastructure").resolve("event").resolve("${domain}EventEntity.kt"))
    PrecompiledTemplateEngine.render("infrastructure/event/DomainEventEntity.jte", templateParams, output)
    output.close()
}