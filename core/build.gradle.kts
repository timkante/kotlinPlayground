plugins {
    `java-library`
}

dependencies {
    api(project(":persist"))

    implementation("org.litote.kmongo:kmongo-coroutine")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core")

    api(platform(project(":platform")))
}