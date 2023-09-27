package me.elgregos.reakteves.cli.generation

import org.gradle.api.DefaultTask
import org.gradle.api.internal.tasks.userinput.UserInputHandler
import org.gradle.api.tasks.SourceSet
import org.gradle.api.tasks.SourceSetContainer
import org.gradle.api.tasks.TaskAction


abstract class DomainClassGenerationTask : DefaultTask() {

    @TaskAction
    fun perform() {
        val inputHandler = services.get(UserInputHandler::class.java)
        val domain = inputHandler.askQuestion("Set the domain name", "Game")
        val domainPackage = inputHandler.askQuestion("Set the domain package", defaultDomainPackage(domain))

        val kotlinSourcePath = project.extensions.getByType(SourceSetContainer::class.java)
            .getByName(SourceSet.MAIN_SOURCE_SET_NAME).allJava.srcDirs.find { it.endsWith("kotlin") }?.toPath() ?: throw Exception("Unable to fin kotlin sourceset path")

        generateDomainEvent(kotlinSourcePath, domain, domainPackage)

    }

    private fun defaultDomainPackage(domain: String) =
        "${project.group}.${Regex("[^A-Za-z0-9 ]").replace(project.rootProject.name, "")}.${domain.lowercase()}"
}