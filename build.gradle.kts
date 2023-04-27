import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.8.20"
    kotlin("plugin.spring") version "1.8.20"
    id("io.spring.dependency-management") version "1.0.12.RELEASE"
    `java-library`
    `maven-publish`
    signing
}

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
    "integrationTestImplementation"(project)
    "integrationTestImplementation"("org.springframework.boot:spring-boot-starter-test:3.0.5")
    "integrationTestImplementation"("org.junit.platform:junit-platform-suite")
    "integrationTestImplementation"("io.projectreactor:reactor-test")
    "integrationTestImplementation"("org.awaitility:awaitility:4.1.1")
    "integrationTestImplementation"("org.awaitility:awaitility-kotlin:4.1.1")
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs = listOf("-Xjsr305=strict")
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

publishing {
    publications {
        create<MavenPublication>("mavenJava") {
            groupId = "me.elgregos"
            artifactId = "events-k"
            version = System.getenv("RELEASE_VERSION")
            pom {
            }
            from(components["java"])
        }
    }
    repositories {
        maven {
            name = "OSSRH"
            url = uri("https://s01.oss.sonatype.org/service/local/staging/deploy/maven2/")
            credentials {
                username = System.getenv("MAVEN_USERNAME")
                password = System.getenv("MAVEN_PASSWORD")
            }
        }
        maven {
            name = "GitHubPackages"
            url = uri("https://maven.pkg.github.com/GregoryBevan/events-k")
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