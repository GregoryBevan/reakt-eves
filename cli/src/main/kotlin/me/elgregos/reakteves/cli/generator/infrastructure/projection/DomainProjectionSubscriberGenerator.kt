package me.elgregos.reakteves.cli.generator.infrastructure.projection

import gg.jte.output.FileOutput
import me.elgregos.reakteves.cli.generator.PrecompiledTemplateEngine
import java.io.File
import java.nio.file.Path

fun generateDomainProjectionSubscriber(kotlinSourcePath: Path, domain: String, domainPackage: String, templateParams: Map<String, String>) {
    val output = FileOutput(kotlinSourcePath.resolve(domainPackage.replace(".", File.separator)).resolve("infrastructure").resolve("projection").resolve("${domain}ProjectionSubscriber.kt"))
    PrecompiledTemplateEngine.render("infrastructure/projection/DomainProjectionSubscriber.jte", templateParams, output)
    output.close()
}