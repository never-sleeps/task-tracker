plugins {
    kotlin("jvm")
    java
    id("com.bmuschko.docker-java-application")
}

dependencies {
    val rabbitVersion: String by project
    val jacksonVersion: String by project
    val logbackVersion: String by project
    val kotlinLoggingJvmVersion: String by project
    val coroutinesVersion: String by project
    val testcontainersVersion: String by project

    implementation(kotlin("stdlib"))
    implementation("com.rabbitmq:amqp-client:$rabbitVersion")
    implementation("com.fasterxml.jackson.core:jackson-databind:$jacksonVersion")
    implementation("ch.qos.logback:logback-classic:$logbackVersion")
    implementation("io.github.microutils:kotlin-logging-jvm:$kotlinLoggingJvmVersion")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:$coroutinesVersion")

    // transport models common
    implementation(project(":common"))

    // v1 api
    implementation(project(":api-jackson"))
    implementation(project(":mappers-jackson"))

    // v2 api
    implementation(project(":api-multiplatform"))
    implementation(project(":mappers-multiplatform"))

    implementation(project(":business"))

    // logging
    implementation(project(":lib-logging-logback"))

    testImplementation("org.testcontainers:rabbitmq:$testcontainersVersion")
    testImplementation(kotlin("test"))
    testImplementation(project(":stubs"))
}

docker {
    javaApplication {
        baseImage.set("openjdk:17")
        ports.set(listOf(8080, 5672))
        images.set(setOf("${project.name}:latest"))
        jvmArgs.set(listOf("-XX:+UseContainerSupport"))
    }
}
