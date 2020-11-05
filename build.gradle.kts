import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    application
    kotlin("jvm") version "1.4.0"
    kotlin("plugin.serialization") version "1.4.0"
}

dependencies {
    runtimeOnly(project(":core"))
}

allprojects {
    group = "kotlin.dev.timkante"
    version = "1.0-SNAPSHOT"

    this.allprojects
        .filterNot {
            it.name == "platform"
        }
        .forEach {
            it.apply {
                plugin("org.jetbrains.kotlin.jvm")
                plugin("org.jetbrains.kotlin.plugin.serialization")

                kotlin.sourceSets["main"].kotlin.srcDirs("src")
                kotlin.sourceSets["test"].kotlin.srcDirs("test")

                sourceSets["main"].resources.srcDirs("resources")
                sourceSets["test"].resources.srcDirs("testresources")

                tasks {
                    withType<KotlinCompile> {
                        kotlinOptions {
                            jvmTarget = "14"
                        }
                    }
                }
            }
        }

    repositories {
        mavenCentral()
    }
}

application {
    mainClass.set("kotlin.dev.timkante.playground.core.Application")
}
