import gg.jte.ContentType

plugins {
    id("kotlin-conventions")
    `java-gradle-plugin`
    id("com.gradle.plugin-publish") version "1.3.0"
    id("gg.jte.gradle").version("3.1.15")
}

repositories {
    mavenCentral()
    maven {
        url = uri("https://s01.oss.sonatype.org/content/repositories/snapshots/")
    }
}

dependencies {
    implementation("gg.jte:jte:3.1.15")
    implementation("org.springframework.boot:spring-boot-starter-webflux") {
        version {
            strictly("[2.7.18,)")
            prefer("3.4.1")
        }
    }
    implementation("org.springframework.boot:spring-boot-starter-data-r2dbc") {
        version {
            strictly("[2.7.18,)")
            prefer("3.4.1")
        }
    }
    "integrationTestImplementation"(gradleTestKit())
}

group = "me.elgregos.reakteves.cli"

gradlePlugin {
    plugins {
        create("ReaKt.EveS Cli Plugin") {
            id = "me.elgregos.reakteves.cli"
            implementationClass = "me.elgregos.reakteves.cli.ReaKtEveSPlugin"
            group = "me.elgregos.reakteves.cli"
            version = System.getenv("RELEASE_VERSION")
            website = "https://github.com/GregoryBevan/reakt-eves/blob/main/cli/README.md"
            vcsUrl = "https://github.com/GregoryBevan/reakt-eves.git"
            displayName = "ReaKt.EveS Cli Plugin"
            description = "Cli to enhance user experience with ReaKt.EveS library"
            tags = setOf("event-sourcing", "cli")
        }
    }
    testSourceSets(sourceSets.integrationTest.get())
}

jte {
    precompile()
    contentType.set(ContentType.Plain)
}

tasks.jar {
    dependsOn(tasks.precompileJte)
    from(fileTree("jte-classes") {
        include("**/*.class")
    })
}