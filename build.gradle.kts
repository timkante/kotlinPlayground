import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    maven
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

    task("generatePom") {
        group = "maven"
        description = "generates a maven pom.xml file based on the build.gradle.kts file"
        doLast {
            maven.pom {
                project {
                    groupId = this@task.group as String
                    artifactId = artifactId
                    version = version
                    withGroovyBuilder {
                        "modules" {
                            subprojects.map {
                                "module"(it.name)
                            }
                        }
                        "inceptionYear"("2042")
                    }
                }
            }.writeTo("pom.xml")
        }
    }

    if (this.name != "platform") {
        apply {
            plugin("org.jetbrains.kotlin.jvm")
            plugin("org.jetbrains.kotlin.plugin.serialization")
        }

        kotlin.sourceSets["main"].kotlin.srcDirs("src")
        kotlin.sourceSets["test"].kotlin.srcDirs("test")

        sourceSets["main"].resources.srcDirs("resources")
        sourceSets["test"].resources.srcDirs("testresources")

        tasks {
            build {
                dependsOn("generatePom")
            }

            withType<KotlinCompile> {
                kotlinOptions {
                    jvmTarget = "14"
                }
            }
        }

        repositories {
            mavenCentral()
        }
    }
}

application {
    mainClass.set("dev.timkante.playground.ApplicationKt")
}
