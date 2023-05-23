import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.8.20"
    kotlin("plugin.spring") version "1.8.20"
    id("io.spring.dependency-management") version "1.0.12.RELEASE"
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

sourceSets {
    create("integrationTest") {
        compileClasspath += sourceSets.test.get().output
        runtimeClasspath += sourceSets.test.get().output
    }
}

configurations["integrationTestRuntimeOnly"].extendsFrom(configurations.testRuntimeOnly.get())
configurations["integrationTestImplementation"].extendsFrom(configurations.testImplementation.get())


extra["testcontainersVersion"] = "1.16.2"
ext["junit-jupiter.version"] = "5.8.2"

dependencyManagement {
    imports {
        mavenBom("org.testcontainers:testcontainers-bom:${property("testcontainersVersion")}")
    }
}

dependencies {
    implementation("com.github.java-json-tools:json-patch:1.13") {
        exclude("com.fasterxml.jackson.core", "jackson-databind")
    }
    implementation("io.github.oshai:kotlin-logging-jvm:4.0.0-beta-27")
    implementation("org.yaml:snakeyaml:2.0")

    implementation("org.springframework.boot:spring-boot-starter-webflux") {
        version {
            strictly("[2.7.11,)")
            prefer("3.0.6")
        }
    }

    implementation("org.springframework.boot:spring-boot-starter-data-r2dbc") {
        version {
            strictly("[2.7.11,)")
            prefer("3.0.6")
        }
    }

    implementation("com.fasterxml.jackson.module:jackson-module-kotlin") {
        version {
            strictly("[2.14,)")
            prefer("2.15.0")
        }
    }
    implementation("com.fasterxml.jackson.datatype:jackson-datatype-jsr310") {
        version {
            strictly("[2.14,)")
            prefer("2.15.0")
        }
    }
    implementation("io.projectreactor.kotlin:reactor-kotlin-extensions:1.2.2") {
        version {
            strictly("[1.1,)")
            prefer("1.2.2")
        }
    }
    implementation("org.postgresql:r2dbc-postgresql:1.0.1.RELEASE")
    annotationProcessor("org.springframework.boot:spring-boot-autoconfigure-processor")

    testImplementation(kotlin("test"))
    testImplementation("io.mockk:mockk:1.13.5")
    testImplementation("com.willowtreeapps.assertk:assertk-jvm:0.25")
    testImplementation("io.projectreactor:reactor-test:3.5.5")


    "integrationTestImplementation"(project)
    "integrationTestImplementation"("org.testcontainers:postgresql")
    "integrationTestImplementation"("io.projectreactor:reactor-test")
    "integrationTestImplementation"("org.awaitility:awaitility:4.1.1")
    "integrationTestImplementation"("org.awaitility:awaitility-kotlin:4.1.1")
    "integrationTestRuntimeOnly"("org.junit.platform:junit-platform-suite")
    "integrationTestRuntimeOnly"("org.testcontainers:junit-jupiter:${property("testcontainersVersion")}")
    "integrationTestRuntimeOnly"("org.testcontainers:r2dbc")
    "integrationTestRuntimeOnly"("org.liquibase:liquibase-core:4.17.2")
    "integrationTestRuntimeOnly"("org.postgresql:postgresql:42.3.8")

    testFixturesApi("org.springframework.boot:spring-boot-starter-test") {
        version {
            strictly("[2.7.11,)")
            prefer("3.0.6")
        }
    }
    testFixturesApi("org.springframework.boot:spring-boot-starter-data-r2dbc") {
        version {
            strictly("[2.7.11,)")
            prefer("3.0.6")
        }
    }
    testFixturesApi("org.testcontainers:junit-jupiter:${property("testcontainersVersion")}")
    testFixturesApi("org.testcontainers:postgresql")
    testFixturesApi("org.testcontainers:r2dbc")
    testFixturesApi("org.junit.platform:junit-platform-suite")
    testFixturesApi("com.fasterxml.jackson.datatype:jackson-datatype-jsr310") {
        version {
            strictly("[2.14,)")
            prefer("2.15.0")
        }
    }
    testFixturesCompileOnly("org.projectlombok:lombok:1.18.26")
    testFixturesAnnotationProcessor("org.projectlombok:lombok:1.18.26")
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs = listOf("-Xjsr305=strict", "-Xjvm-default=all")
        jvmTarget = "17"
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
    testLogging {
        events("passed", "skipped", "failed")
    }
}

val integrationTest = tasks.register<Test>("integrationTest") {
    description = "Runs the integration tests."
    group = "verification"
    testClassesDirs = sourceSets["integrationTest"].output.classesDirs
    classpath = sourceSets["integrationTest"].runtimeClasspath
    shouldRunAfter("test")
}

tasks.check { dependsOn(integrationTest) }

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
            url = uri(if(version.toString().endsWith("-SNAPSHOT")) "https://s01.oss.sonatype.org/service/local/staging/deploy/maven2/" else "https://s01.oss.sonatype.org/content/repositories/snapshots/")
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