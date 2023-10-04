package me.elgregos.reakteves.cli

import me.elgregos.reakteves.cli.task.DomainClassGenerationTask
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.logging.LogLevel
import org.gradle.api.logging.configuration.ConsoleOutput

class ReaKtEveSPlugin: Plugin<Project> {

    override fun apply(project: Project) {
        project.gradle.startParameter.logLevel = LogLevel.QUIET
        project.gradle.startParameter.consoleOutput = ConsoleOutput.Plain
        project.tasks.register("initDomain", DomainClassGenerationTask::class.java)
    }
}