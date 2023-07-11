plugins {
    kotlin("jvm")
}

dependencies {
    val testcontainersVersion: String by project
    val kotestVersion: String by project
    val ktorVersion: String by project
    val coroutinesVersion: String by project
    val logbackVersion: String by project
    val kotlinLoggingJvmVersion: String by project
    val ktorClientOkhttpVersion: String by project
    val rabbitVersion: String by project
    val kafkaVersion: String by project

    implementation(kotlin("stdlib"))
    implementation("io.ktor:ktor-client-okhttp-jvm:$ktorClientOkhttpVersion")

    // project modules
    implementation(project(":api-jackson"))
    implementation(project(":api-multiplatform"))

    implementation("ch.qos.logback:logback-classic:$logbackVersion")
    implementation("io.github.microutils:kotlin-logging-jvm:$kotlinLoggingJvmVersion")

    testImplementation("io.kotest:kotest-runner-junit5:$kotestVersion")
    testImplementation("io.kotest:kotest-assertions-core:$kotestVersion")
    testImplementation("io.kotest:kotest-framework-datatest:$kotestVersion")
    testImplementation("io.kotest:kotest-property:$kotestVersion")

    implementation("com.rabbitmq:amqp-client:$rabbitVersion")
    implementation("org.apache.kafka:kafka-clients:$kafkaVersion")

    testImplementation("org.testcontainers:testcontainers:$testcontainersVersion")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:$coroutinesVersion")

    testImplementation("io.ktor:ktor-client-core:$ktorVersion")
    testImplementation("io.ktor:ktor-client-okhttp:$ktorVersion")
}

var severity: String = "MINOR"

tasks {
    withType<Test>().configureEach {
        useJUnitPlatform()
        dependsOn(":app-spring:dockerBuildImage")
        dependsOn(":app-ktor:publishImageToLocalRegistry")
        dependsOn(":app-rabbitmq:dockerBuildImage")
        dependsOn(":app-kafka:dockerBuildImage")
    }
}
