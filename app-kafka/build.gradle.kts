plugins {
    application
    kotlin("jvm")
    id("com.bmuschko.docker-java-application")
}

application {
    mainClass.set("me.neversleeps.kafka.MainKt")
}

docker {
    javaApplication {
        mainClassName.set(application.mainClass.get())
        baseImage.set("openjdk:17")
        maintainer.set("(c) neversleeps")
        ports.set(listOf(8080, 9093))
        val imageName = project.name
        images.set(
            listOf(
                "$imageName:${project.version}",
                "$imageName:latest",
            ),
        )
        jvmArgs.set(listOf("-Xms256m", "-Xmx512m"))
    }
}

dependencies {
    val kafkaVersion: String by project
    val coroutinesVersion: String by project
    val atomicfuVersion: String by project
    val logbackVersion: String by project
    val kotlinLoggingJvmVersion: String by project
    implementation("org.apache.kafka:kafka-clients:$kafkaVersion")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:$coroutinesVersion")
    implementation("org.jetbrains.kotlinx:atomicfu:$atomicfuVersion")

    // log
    implementation("ch.qos.logback:logback-classic:$logbackVersion")
    implementation("io.github.microutils:kotlin-logging-jvm:$kotlinLoggingJvmVersion")

    // transport models
    implementation(project(":common"))
    implementation(project(":api-jackson"))
    implementation(project(":api-multiplatform"))
    implementation(project(":mappers-jackson"))
    implementation(project(":mappers-multiplatform"))
    // logic
    implementation(project(":business"))
    // logging
    implementation(project(":lib-logging-logback"))

    testImplementation(kotlin("test-junit"))
}
