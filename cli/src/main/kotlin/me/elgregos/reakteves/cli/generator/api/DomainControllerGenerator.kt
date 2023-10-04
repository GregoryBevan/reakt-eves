package me.elgregos.reakteves.cli.generator.api

import gg.jte.output.FileOutput
import me.elgregos.reakteves.cli.generator.PrecompiledTemplateEngine
import java.io.File
import java.nio.file.Path

fun generateDomainController(kotlinSourcePath: Path, domain: String, domainPackage: String, templateParams: Map<String, String>) {
    val output = FileOutput(kotlinSourcePath.resolve(domainPackage.replace(".", File.separator)).resolve("api").resolve("${domain}Controller.kt"))
    PrecompiledTemplateEngine.render("api/DomainController.jte", templateParams, output)
    output.close()
}