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
    api("org.apache.commons:commons-math3:3.6.1")
    // This dependency is used internally, and not exposed to consumers on their own compile classpath.
    implementation("com.google.guava:guava:31.1-jre")
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