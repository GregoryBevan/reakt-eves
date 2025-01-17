package me.elgregos.reakteves.cli.task

import me.elgregos.reakteves.cli.generator.api.generateDomainController
import me.elgregos.reakteves.cli.generator.application.generateDomainCommand
import me.elgregos.reakteves.cli.generator.application.generateDomainCommandHandler
import me.elgregos.reakteves.cli.generator.application.generateDomainProjectionService
import me.elgregos.reakteves.cli.generator.database.generateDomainChangeLog
import me.elgregos.reakteves.cli.generator.domain.entity.generateDomain
import me.elgregos.reakteves.cli.generator.domain.event.generateDomainAggregate
import me.elgregos.reakteves.cli.generator.domain.event.generateDomainEvent
import me.elgregos.reakteves.cli.generator.domain.event.generateDomainEventRepository
import me.elgregos.reakteves.cli.generator.infrastructure.config.generateDomainStoreConfig
import me.elgregos.reakteves.cli.generator.infrastructure.event.generateDomainEventEntity
import me.elgregos.reakteves.cli.generator.infrastructure.event.generateDomainEventEntityRepository
import me.elgregos.reakteves.cli.generator.infrastructure.projection.generateDomainEntity
import me.elgregos.reakteves.cli.generator.infrastructure.projection.generateDomainProjectionRepository
import me.elgregos.reakteves.cli.generator.infrastructure.projection.generateDomainProjectionSubscriber
import me.elgregos.reakteves.cli.generator.templateParams
import org.gradle.api.DefaultTask
import org.gradle.api.internal.tasks.userinput.UserInputHandler
import org.gradle.api.tasks.SourceSet
import org.gradle.api.tasks.SourceSetContainer
import org.gradle.api.tasks.TaskAction
import java.nio.file.Path


abstract class DomainClassGenerationTask : DefaultTask() {

    private var projectDirPath: String
    private var projectGroup: String
    private var rootProjectName: String
    private var kotlinSourcePath: Path

    private lateinit var domain: String
    private lateinit var domainPackage: String

    init {
        kotlinSourcePath = (project.extensions.findByName("sourceSets") as SourceSetContainer)
            .getByName(SourceSet.MAIN_SOURCE_SET_NAME).allJava.srcDirs.find { it.endsWith("kotlin") }?.toPath()
            ?: throw Exception("Unable to fin kotlin sourceset path")
        projectDirPath = project.projectDir.path
        projectGroup = "${project.group}"
        rootProjectName = project.rootProject.name
    }

    @TaskAction
    fun perform() {
        val inputHandler = services.get(UserInputHandler::class.java)
        domain = inputHandler.askUser { it.askQuestion("Set the domain entity name in camel case", "Game") }.get()
        domainPackage = inputHandler.askUser { it.askQuestion("Set the domain package", defaultDomainPackage(domain)) }.get()
        val templateParams = templateParams(domain, domainPackage)
        generateDomainChangeLog(projectDirPath, projectGroup, templateParams)
        generateDomainController(kotlinSourcePath, domain, domainPackage, templateParams)
        generateDomainCommand(kotlinSourcePath, domain, domainPackage, templateParams)
        generateDomainCommandHandler(kotlinSourcePath, domain, domainPackage, templateParams)
        generateDomainProjectionService(kotlinSourcePath, domain, domainPackage, templateParams)
        generateDomain(kotlinSourcePath, domain, domainPackage, templateParams)
        generateDomainAggregate(kotlinSourcePath, domain, domainPackage, templateParams)
        generateDomainEvent(kotlinSourcePath, domain, domainPackage, templateParams)
        generateDomainEventRepository(kotlinSourcePath, domain, domainPackage, templateParams)
        generateDomainStoreConfig(kotlinSourcePath, domain, domainPackage, templateParams)
        generateDomainEventEntity(kotlinSourcePath, domain, domainPackage, templateParams)
        generateDomainEventEntityRepository(kotlinSourcePath, domain, domainPackage, templateParams)
        generateDomainEntity(kotlinSourcePath, domain, domainPackage, templateParams)
        generateDomainProjectionRepository(kotlinSourcePath, domain, domainPackage, templateParams)
        generateDomainProjectionSubscriber(kotlinSourcePath, domain, domainPackage, templateParams)

    }

    private fun defaultDomainPackage(domain: String) =
        "${projectGroup}.${Regex("[^A-Za-z0-9 ]").replace(rootProjectName, "")}.${domain.lowercase()}"
}