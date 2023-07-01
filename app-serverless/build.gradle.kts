val jacksonVersion: String by project
val serializationVersion: String by project
val yandexCloudSdkVersion: String by project

plugins {
    kotlin("jvm")
    id("com.github.johnrengelman.shadow")
}

dependencies {
    implementation("com.yandex.cloud:java-sdk-functions:$yandexCloudSdkVersion")
    implementation(kotlin("stdlib-jdk8"))

    implementation("com.fasterxml.jackson.core:jackson-core:$jacksonVersion")
    implementation("com.fasterxml.jackson.core:jackson-databind:$jacksonVersion")
    implementation("com.fasterxml.jackson.core:jackson-annotations:$jacksonVersion")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin:$jacksonVersion")

    implementation("org.jetbrains.kotlinx:kotlinx-serialization-core:$serializationVersion")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:$serializationVersion")

    // transport models
    implementation(project(":common"))
    implementation(project(":api-jackson"))
    implementation(project(":api-multiplatform"))
    implementation(project(":mappers-jackson"))
    implementation(project(":mappers-multiplatform"))

    // Stubs
    implementation(project(":stubs"))
}