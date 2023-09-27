package me.elgregos.reakteves.cli.generator

import gg.jte.ContentType
import gg.jte.TemplateEngine
import gg.jte.TemplateEngine.createPrecompiled


val PrecompiledTemplateEngine: TemplateEngine = createPrecompiled(ContentType.Plain)

fun templateParams(domain: String, domainPackage: String) = mapOf(
    "domain" to domain,
    "domainPackage" to domainPackage,
    "domainPrefix" to domain.replaceFirstChar { it.lowercase() },
    "domainPath" to domain.camelToKebabCase()
)

fun String.camelToKebabCase(): String {
    val pattern = "(?<=.)[A-Z]".toRegex()
    return this.replace(pattern, "-$0").lowercase()
}
