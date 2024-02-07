import org.gradle.api.publish.PublishingExtension

plugins {
    id("java")
    java
    `maven-publish`
}

group = "org.sk_dev"
version = "1.0.1"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.9.1"))
    testImplementation("org.junit.jupiter:junit-jupiter")
}

configure<PublishingExtension> {
    repositories {
        maven {
            name = "CronParser"
            url = uri("https://maven.pkg.github.com/skpub/CronParser")
            credentials {
                username = System.getenv("GITHUB_USERNAME")
                password = System.getenv("GITHUB_TOKEN")
            }
        }
        publications {
            register<MavenPublication>("gpr") {
                artifactId = "cronparser"
                from(components["java"])
            }
        }
    }
}

tasks.test {
    useJUnitPlatform()
}