plugins {
    id("kotlin-conventions")
    kotlin("plugin.spring") version "2.0.21"
    id("io.spring.dependency-management") version "1.1.7"
    `java-library`
    `maven-publish`
    signing
    id("io.github.gradle-nexus.publish-plugin") version "2.0.0"
    `java-test-fixtures`
    id("org.owasp.dependencycheck") version "12.0.0"
}

group = "me.elgregos"
version = System.getenv("RELEASE_VERSION")
java.sourceCompatibility = JavaVersion.VERSION_17


repositories {
    mavenCentral()
}

extra["testcontainersVersion"] = "1.20.4"
ext["junit-jupiter.version"] = "5.11.4"

dependencyManagement {
    imports {
        mavenBom("org.testcontainers:testcontainers-bom:${property("testcontainersVersion")}")
    }
}

dependencies {
    implementation("com.github.java-json-tools:json-patch:1.13") {
        exclude("com.fasterxml.jackson.core", "jackson-databind")
    }
    api("io.github.oshai:kotlin-logging-jvm:7.0.3")
    implementation("org.yaml:snakeyaml:2.3")

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

    implementation("com.fasterxml.jackson.module:jackson-module-kotlin") {
        version {
            strictly("[2.14.3,)")
            prefer("2.18.2")
        }
    }
    implementation("com.fasterxml.jackson.datatype:jackson-datatype-jsr310") {
        version {
            strictly("[2.14.3,)")
            prefer("2.7.18")
        }
    }
    implementation("io.projectreactor.kotlin:reactor-kotlin-extensions") {
        version {
            strictly("[1.1,)")
            prefer("1.2.3")
        }
    }
    implementation("org.postgresql:r2dbc-postgresql:1.0.7.RELEASE")
    annotationProcessor("org.springframework.boot:spring-boot-autoconfigure-processor")

    testImplementation(kotlin("test"))
    testImplementation("io.mockk:mockk:1.13.16")
    testImplementation("com.willowtreeapps.assertk:assertk-jvm:0.28.1")
    testImplementation("io.projectreactor:reactor-test:3.7.1")

    integrationTestImplementation("org.testcontainers:postgresql")
    integrationTestImplementation("io.projectreactor:reactor-test")
    integrationTestImplementation("org.awaitility:awaitility:4.2.2")
    integrationTestImplementation("org.awaitility:awaitility-kotlin:4.2.2")
    integrationTestRuntimeOnly("org.junit.platform:junit-platform-suite")
    integrationTestRuntimeOnly("org.testcontainers:junit-jupiter:${property("testcontainersVersion")}")
    integrationTestRuntimeOnly("org.testcontainers:r2dbc")
    integrationTestRuntimeOnly("org.liquibase:liquibase-core:4.30.0")
    integrationTestRuntimeOnly("org.postgresql:postgresql:42.7.4")

    testFixturesApi("org.springframework.boot:spring-boot-starter-test") {
        version {
            strictly("[2.7.18,)")
            prefer("3.4.1")
        }
    }
    testFixturesApi("org.springframework.boot:spring-boot-starter-data-r2dbc") {
        version {
            strictly("[2.7.18,)")
            prefer("3.4.1")
        }
    }
    testFixturesApi("org.testcontainers:junit-jupiter:${property("testcontainersVersion")}")
    testFixturesApi("org.testcontainers:postgresql")
    testFixturesApi("org.testcontainers:r2dbc")
    testFixturesApi("org.junit.platform:junit-platform-suite")
    testFixturesApi("com.fasterxml.jackson.datatype:jackson-datatype-jsr310") {
        version {
            strictly("[2.14,)")
            prefer("2.18.2")
        }
    }
    testFixturesCompileOnly("org.projectlombok:lombok:1.18.36")
    testFixturesAnnotationProcessor("org.projectlombok:lombok:1.18.36")
}

java {
    withJavadocJar()
    withSourcesJar()
}

nexusPublishing {
    repositories.sonatype {
        stagingProfileId.set(System.getenv("SONATYPE_STAGING_PROFILE_ID"))
        username.set(System.getenv("MAVEN_USERNAME"))
        password.set(System.getenv("MAVEN_PASSWORD"))
        nexusUrl.set(uri("https://s01.oss.sonatype.org/service/local/"))
        snapshotRepositoryUrl.set(uri("https://s01.oss.sonatype.org/content/repositories/snapshots/"))
    }
}

val javaComponent = components["java"] as AdhocComponentWithVariants
javaComponent.withVariantsFromConfiguration(configurations["testFixturesApiElements"]) { skip() }
javaComponent.withVariantsFromConfiguration(configurations["testFixturesRuntimeElements"]) { skip() }

publishing {
    publications {
        create<MavenPublication>("local") {
        }
    }
    publications {
        create<MavenPublication>("mavenJava") {
            groupId = "me.elgregos"
            artifactId = "reakt-eves"
            version = System.getenv("RELEASE_VERSION")
            pom {
                name.set("reakt-eves")
                description.set("Kotlin reactive library to ease event sourcing pattern integration in your Spring Webflux / Reactor projects")
                url.set("https://github.com/GregoryBevan/reakt-eves.git")
                licenses {
                    license {
                        name.set("MIT License")
                        url.set("https://opensource.org/license/mit/")
                    }
                }
                developers {
                    developer {
                        id.set("GregoryBevan")
                        name.set("Grégory Bévan")
                        email.set("gregory.bevan@zenika.com")
                    }
                }
                scm {
                    connection.set("scm:git:git://github.com:GregoryBevan/reakt-eves.git")
                    developerConnection.set("scm:git:ssh://git@github.com:GregoryBevan/reakt-eves.git")
                    url.set("https://github.com/GregoryBevan/reakt-eves.git")
                }
            }
            from(javaComponent)
        }
    }
    repositories {
        maven {
            name = "OSSRH"
            url = uri(if(version.toString().endsWith("-SNAPSHOT")) "https://s01.oss.sonatype.org/content/repositories/snapshots/" else "https://s01.oss.sonatype.org/service/local/staging/deploy/maven2/" )
            credentials {
                username = System.getenv("MAVEN_USERNAME")
                password = System.getenv("MAVEN_PASSWORD")
            }
        }
    }
}

signing {
    useInMemoryPgpKeys(System.getenv("GPG_KEY"), System.getenv("GPG_PASSWORD"))
    sign(publishing.publications["mavenJava"])
}