plugins {
    id("kotlin-conventions")
    kotlin("plugin.spring") version "1.9.10"
    id("io.spring.dependency-management") version "1.1.3"
    `java-library`
    `maven-publish`
    signing
    id("io.github.gradle-nexus.publish-plugin") version "1.3.0"
    `java-test-fixtures`
}

group = "me.elgregos"
version = System.getenv("RELEASE_VERSION")
java.sourceCompatibility = JavaVersion.VERSION_17


repositories {
    mavenCentral()
}

extra["testcontainersVersion"] = "1.19.0"
ext["junit-jupiter.version"] = "5.9.3"

dependencyManagement {
    imports {
        mavenBom("org.testcontainers:testcontainers-bom:${property("testcontainersVersion")}")
    }
}

dependencies {
    implementation("com.github.java-json-tools:json-patch:1.13") {
        exclude("com.fasterxml.jackson.core", "jackson-databind")
    }
    implementation("io.github.oshai:kotlin-logging-jvm:5.1.0")
    implementation("org.yaml:snakeyaml:2.0")

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

    implementation("com.fasterxml.jackson.module:jackson-module-kotlin") {
        version {
            strictly("[2.14,)")
            prefer("2.15.2")
        }
    }
    implementation("com.fasterxml.jackson.datatype:jackson-datatype-jsr310") {
        version {
            strictly("[2.14,)")
            prefer("2.15.2")
        }
    }
    implementation("io.projectreactor.kotlin:reactor-kotlin-extensions") {
        version {
            strictly("[1.1,)")
            prefer("1.2.2")
        }
    }
    implementation("org.postgresql:r2dbc-postgresql:1.0.2.RELEASE")
    annotationProcessor("org.springframework.boot:spring-boot-autoconfigure-processor")

    testImplementation(kotlin("test"))
    testImplementation("io.mockk:mockk:1.13.7")
    testImplementation("com.willowtreeapps.assertk:assertk-jvm:0.27.0")
    testImplementation("io.projectreactor:reactor-test:3.5.10")

    "integrationTestImplementation"("org.testcontainers:postgresql")
    "integrationTestImplementation"("io.projectreactor:reactor-test")
    "integrationTestImplementation"("org.awaitility:awaitility:4.2.0")
    "integrationTestImplementation"("org.awaitility:awaitility-kotlin:4.2.0")
    "integrationTestRuntimeOnly"("org.junit.platform:junit-platform-suite")
    "integrationTestRuntimeOnly"("org.testcontainers:junit-jupiter:${property("testcontainersVersion")}")
    "integrationTestRuntimeOnly"("org.testcontainers:r2dbc")
    "integrationTestRuntimeOnly"("org.liquibase:liquibase-core:4.23.2")
    "integrationTestRuntimeOnly"("org.postgresql:postgresql:42.6.0")

    testFixturesApi("org.springframework.boot:spring-boot-starter-test") {
        version {
            strictly("[2.7.15,)")
            prefer("3.1.3")
        }
    }
    testFixturesApi("org.springframework.boot:spring-boot-starter-data-r2dbc") {
        version {
            strictly("[2.7.15,)")
            prefer("3.1.3")
        }
    }
    testFixturesApi("org.testcontainers:junit-jupiter:${property("testcontainersVersion")}")
    testFixturesApi("org.testcontainers:postgresql")
    testFixturesApi("org.testcontainers:r2dbc")
    testFixturesApi("org.junit.platform:junit-platform-suite")
    testFixturesApi("com.fasterxml.jackson.datatype:jackson-datatype-jsr310") {
        version {
            strictly("[2.14,)")
            prefer("2.15.2")
        }
    }
    testFixturesCompileOnly("org.projectlombok:lombok:1.18.28")
    testFixturesAnnotationProcessor("org.projectlombok:lombok:1.18.28")
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
        maven {
            name = "GitHubPackages"
            url = uri("https://maven.pkg.github.com/GregoryBevan/reakt-eves")
            credentials {
                username = System.getenv("GITHUB_ACTOR")
                password = System.getenv("GITHUB_TOKEN")
            }
        }
    }
}

signing {
    useInMemoryPgpKeys(System.getenv("GPG_KEY"), System.getenv("GPG_PASSWORD"))
    sign(publishing.publications["mavenJava"])
}