import gg.jte.ContentType

plugins {
    id("kotlin-conventions")
    `java-gradle-plugin`
    id("com.gradle.plugin-publish") version "1.1.0"
    `maven-publish`
    id("gg.jte.gradle").version("3.1.0")
}

repositories {
    mavenCentral()
    maven {
        url = uri("https://s01.oss.sonatype.org/content/repositories/snapshots/")
    }
}

dependencies {
    implementation("me.elgregos:reakt-eves:1.2.0-SNAPSHOT")
    implementation("gg.jte:jte:3.1.0")
    implementation("org.springframework.boot:spring-boot-starter-webflux") {
        version {
            strictly("[2.7.15,)")
            prefer("3.1.3")
        }
    }
    implementation("org.springframework.boot:spring-boot-starter-data-r2dbc") {
        version {
            strictly("[2.7.15,)")
            prefer("3.1.3")
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
            version = "0.0.1"
            website = "https://github.com/GregoryBevan/reakt-eves/cli"
            vcsUrl = "https://github.com/GregoryBevan/reakt-eves.git"
            displayName = "ReaKt.EveS Cli Plugin"
            description = "Cli to enhance user experience with ReaKt.EveS library"
            tags = setOf("event-sourcing", "cli")
        }
    }
    testSourceSets(sourceSets.integrationTest.get())
}

publishing {
    repositories {
        maven {
            url = uri("${layout.buildDirectory}/repo")
        }
    }
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