package me.elgregos.reakteves.cli.task

import me.elgregos.reakteves.cli.generator.api.generateDomainController
import me.elgregos.reakteves.cli.generator.application.generateDomainCommand
import me.elgregos.reakteves.cli.generator.application.generateDomainCommandHandler
import me.elgregos.reakteves.cli.generator.application.generateDomainProjectionService
import me.elgregos.reakteves.cli.generator.domain.entity.generateDomain
import me.elgregos.reakteves.cli.generator.domain.event.generateDomainAggregate
import me.elgregos.reakteves.cli.generator.domain.event.generateDomainEvent
import me.elgregos.reakteves.cli.generator.domain.event.generateDomainEventRepository
import me.elgregos.reakteves.cli.generator.templateParams
import org.gradle.api.DefaultTask
import org.gradle.api.internal.tasks.userinput.UserInputHandler
import org.gradle.api.tasks.SourceSet
import org.gradle.api.tasks.SourceSetContainer
import org.gradle.api.tasks.TaskAction


abstract class DomainClassGenerationTask : DefaultTask() {

    @TaskAction
    fun perform() {
        val inputHandler = services.get(UserInputHandler::class.java)
        val domain = inputHandler.askQuestion("Set the domain entity name in camel case", "Game")
        val domainPackage = inputHandler.askQuestion("Set the domain package", defaultDomainPackage(domain))

        val kotlinSourcePath = project.extensions.getByType(SourceSetContainer::class.java)
            .getByName(SourceSet.MAIN_SOURCE_SET_NAME).allJava.srcDirs.find { it.endsWith("kotlin") }?.toPath()
            ?: throw Exception("Unable to fin kotlin sourceset path")

        val templateParams = templateParams(domain, domainPackage)
        println("YO MAMA: ${templateParams.entries.joinToString()}")
        generateDomainController(kotlinSourcePath, domain, domainPackage, templateParams)
        generateDomainCommand(kotlinSourcePath, domain, domainPackage, templateParams)
        generateDomainCommandHandler(kotlinSourcePath, domain, domainPackage, templateParams)
        generateDomainProjectionService(kotlinSourcePath, domain, domainPackage, templateParams)
        generateDomain(kotlinSourcePath, domain, domainPackage, templateParams)
        generateDomainAggregate(kotlinSourcePath, domain, domainPackage, templateParams)
        generateDomainEvent(kotlinSourcePath, domain, domainPackage, templateParams)
        generateDomainEventRepository(kotlinSourcePath, domain, domainPackage, templateParams)

    }

    private fun defaultDomainPackage(domain: String) =
        "${project.group}.${Regex("[^A-Za-z0-9 ]").replace(project.rootProject.name, "")}.${domain.lowercase()}"
}