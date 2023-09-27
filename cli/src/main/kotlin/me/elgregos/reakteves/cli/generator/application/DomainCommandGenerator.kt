package me.elgregos.reakteves.cli.generator.application

import gg.jte.output.FileOutput
import me.elgregos.reakteves.cli.generator.PrecompiledTemplateEngine
import java.io.File
import java.nio.file.Path

fun generateDomainCommand(kotlinSourcePath: Path, domain: String, domainPackage: String, templateParams: Map<String, String>) {
    val output = FileOutput(kotlinSourcePath.resolve(domainPackage.replace(".", File.separator)).resolve("application").resolve("${domain}Commmand.kt"))
    PrecompiledTemplateEngine.render("application/DomainCommand.jte", templateParams, output)
    output.close()
}