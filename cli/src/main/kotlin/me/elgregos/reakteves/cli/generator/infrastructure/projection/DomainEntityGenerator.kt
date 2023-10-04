package me.elgregos.reakteves.cli.generator.infrastructure.projection

import gg.jte.output.FileOutput
import me.elgregos.reakteves.cli.generator.PrecompiledTemplateEngine
import java.io.File
import java.nio.file.Path

fun generateDomainEntity(kotlinSourcePath: Path, domain: String, domainPackage: String, templateParams: Map<String, String>) {
    val output = FileOutput(kotlinSourcePath.resolve(domainPackage.replace(".", File.separator)).resolve("infrastructure").resolve("projection").resolve("${domain}Entity.kt"))
    PrecompiledTemplateEngine.render("infrastructure/projection/DomainEntity.jte", templateParams, output)
    output.close()
}