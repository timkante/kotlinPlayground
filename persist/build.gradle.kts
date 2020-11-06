plugins {
    `java-library`
    maven
}

dependencies {
    api(platform(project(":platform")))

    implementation("org.litote.kmongo:kmongo-coroutine")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-core")
}
