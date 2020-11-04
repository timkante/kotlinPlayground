import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.4.0"
    kotlin("plugin.serialization") version "1.4.0"
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation(kotlin("stdlib"))
    implementation("org.litote.kmongo:kmongo-coroutine:4.1.2")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-core:1.0.0-RC")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.4.0-M1")
}

tasks {
    withType<KotlinCompile> {
        kotlinOptions {
            jvmTarget = "14"
        }
    }
}
