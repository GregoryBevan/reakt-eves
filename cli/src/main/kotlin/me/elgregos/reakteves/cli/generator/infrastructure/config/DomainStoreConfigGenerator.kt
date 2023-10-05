package me.elgregos.reakteves.cli.generator.infrastructure.config

import gg.jte.output.FileOutput
import me.elgregos.reakteves.cli.generator.PrecompiledTemplateEngine
import java.io.File
import java.nio.file.Path

fun generateDomainStoreConfig(kotlinSourcePath: Path, domain: String, domainPackage: String, templateParams: Map<String, String>) {
    val output = FileOutput(kotlinSourcePath.resolve(domainPackage.replace(".", File.separator)).resolve("infrastructure").resolve("config").resolve("${domain}StoreConfig.kt"))
    PrecompiledTemplateEngine.render("infrastructure/config/DomainStoreConfig.jte", templateParams, output)
    output.close()
}