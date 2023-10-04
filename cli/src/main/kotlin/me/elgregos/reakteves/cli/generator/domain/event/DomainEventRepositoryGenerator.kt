package me.elgregos.reakteves.cli.generator.domain.event

import gg.jte.output.FileOutput
import me.elgregos.reakteves.cli.generator.PrecompiledTemplateEngine
import java.io.File
import java.nio.file.Path

fun generateDomainEventRepository(kotlinSourcePath: Path, domain: String, domainPackage: String, templateParams: Map<String, String>) {
    val output = FileOutput(kotlinSourcePath.resolve(domainPackage.replace(".", File.separator)).resolve("domain").resolve("event").resolve("${domain}EventRepository.kt"))
    PrecompiledTemplateEngine.render("domain/event/DomainEventRepository.jte", templateParams, output)
    output.close()
}