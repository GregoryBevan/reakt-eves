package me.elgregos.reakteves.cli.generator

import gg.jte.ContentType
import gg.jte.TemplateEngine
import gg.jte.output.StringOutput
import gg.jte.resolve.DirectoryCodeResolver
import java.nio.file.Paths

internal open class GeneratorTest {
    internal val testTemplateEngine: TemplateEngine =
        TemplateEngine.create(DirectoryCodeResolver(Paths.get("src/main/jte")), ContentType.Plain)

    internal val output = StringOutput()

    val domainPackageEntry = "domainPackage" to "com.elgregos.escape.camp.game"
    val domainEntry = "domain" to "Game"
    val domainPathEntry =  "domainPath" to "game"
    val domainPrefixEntry =  "domainPrefix" to "game"
    val domainTableEntry =  "domainTable" to "game"
}