plugins {
    kotlin("jvm") version "1.8.20"
    `java-library`
    `maven-publish`
}

repositories {
    mavenCentral()
}

dependencies {
    // This dependency is exported to consumers, that is to say found on their compile classpath.
    api("org.springframework.boot:spring-boot-starter-data-r2dbc:3.0.5")
    api("com.fasterxml.jackson.module:jackson-module-kotlin:2.14.2")
    api("io.projectreactor.kotlin:reactor-kotlin-extensions:1.2.2")
    // This dependency is used internally, and not exposed to consumers on their own compile classpath.
    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()
    doFirst {
        println("*** ENVIRONMENT VARIABLE DUMP ***")
        environment.forEach { (k, v) -> println("${k}:${v}") }

    }
}

publishing {
    repositories {
        maven {
            name = "GitHubPackages"
            url = uri("https://maven.pkg.github.com/GregoryBevan/events-k")
            credentials {
                username = System.getenv("GITHUB_ACTOR")
                password = System.getenv("GITHUB_TOKEN")
            }
        }
    }
    publications {
        create<MavenPublication>("maven") {
            groupId = "me.elgregos"
            artifactId = "events-k"
            version = System.getenv("RELEASE_VERSION")

            from(components["java"])
        }
    }
}