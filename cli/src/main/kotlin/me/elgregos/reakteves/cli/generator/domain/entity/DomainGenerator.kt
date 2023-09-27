package me.elgregos.reakteves.cli.generator.domain.entity

import gg.jte.output.FileOutput
import me.elgregos.reakteves.cli.generator.PrecompiledTemplateEngine
import java.io.File
import java.nio.file.Path

fun generateDomain(kotlinSourcePath: Path, domain: String, domainPackage: String, templateParams: Map<String, String>) {
    val output = FileOutput(kotlinSourcePath.resolve(domainPackage.replace(".", File.separator)).resolve("domain").resolve("entity").resolve("${domain}.kt"))
    PrecompiledTemplateEngine.render("domain/entity/Domain.jte", templateParams, output)
    output.close()
}