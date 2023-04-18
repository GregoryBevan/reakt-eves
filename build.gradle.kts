plugins {
    kotlin("jvm") version "1.8.20"
    `java-library`
    `maven-publish`
    id("io.spring.dependency-management") version "1.0.12.RELEASE"
}

repositories {
    mavenCentral()
}

dependencies {
    api("com.github.java-json-tools:json-patch:1.13")
    api("io.github.oshai:kotlin-logging-jvm:4.0.0-beta-22")

    implementation("org.springframework.boot:spring-boot-starter-webflux")  {
        version {
            strictly("[2.7,)")
            prefer("3.0.5")
        }
    }
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("com.fasterxml.jackson.datatype:jackson-datatype-jsr310")
    implementation("io.projectreactor.kotlin:reactor-kotlin-extensions:1.2.2")

    testImplementation(kotlin("test"))
    testImplementation("io.mockk:mockk:1.13.5")
    testImplementation("com.willowtreeapps.assertk:assertk-jvm:0.25")
    testImplementation("io.projectreactor:reactor-test:3.5.5")
}

tasks.test {
    useJUnitPlatform()
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