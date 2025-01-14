package me.elgregos.reakteves.cli

import assertk.assertThat
import assertk.assertions.isEqualTo
import org.gradle.internal.impldep.org.junit.rules.TemporaryFolder
import org.gradle.testkit.runner.GradleRunner
import org.gradle.testkit.runner.TaskOutcome
import org.junit.jupiter.api.Disabled
import kotlin.test.BeforeTest
import kotlin.test.Test


@Disabled
class ReaKtEveSPluginTest {

    lateinit var testProjectDir: TemporaryFolder

    @BeforeTest
    fun setUp() {
        testProjectDir = TemporaryFolder()
        testProjectDir.create()
        testProjectDir.newFile("build.gradle.kts")
            .appendText(
                """
            plugins {
                kotlin("jvm") version "2.0.21"
                id("me.elgregos.reakteves.cli")
            }
            
            group = "me.elgregos"
            """.trimIndent()
            )
        testProjectDir.newFile("settings.gradle.kts")
            .appendText(
                """
               rootProject.name = "escapecamp"
           """.trimIndent()
            )
    }

    @Test
    fun `should successfully create domain classes`() {
        val buildResult = GradleRunner.create()
            .withPluginClasspath()
            .withProjectDir(testProjectDir.root)
            .withTestKitDir(testProjectDir.newFolder())
            .withArguments("initDomain")
            .withDebug(true)
            .build()

        assertThat(buildResult.task(":initDomain")?.outcome).isEqualTo(TaskOutcome.SUCCESS)
    }
}