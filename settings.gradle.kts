rootProject.name = "task-tracker"

pluginManagement {
    val kotlinVersion: String by settings
    val openapiVersion: String by settings

    // spring
    val springBootVersion: String by settings
    val springDependencyManagementVersion: String by settings
    val pluginSpringVersion: String by settings
    val pluginJpaVersion: String by settings

    val bmuschkoVersion: String by settings

    // ktor
    val ktorVersion: String by settings

    val pluginShadow: String by settings

    plugins {
        kotlin("jvm") version kotlinVersion apply false
        kotlin("plugin.serialization") version kotlinVersion apply false

        // spring
        id("org.springframework.boot") version springBootVersion apply false
        id("io.spring.dependency-management") version springDependencyManagementVersion apply false
        kotlin("plugin.spring") version pluginSpringVersion apply false
        kotlin("plugin.jpa") version pluginJpaVersion apply false

        // openapi
        id("org.openapi.generator") version openapiVersion apply false

        // docker
        id("com.bmuschko.docker-java-application") version bmuschkoVersion apply false
        id("com.bmuschko.docker-spring-boot-application") version bmuschkoVersion apply false
        id("com.bmuschko.docker-remote-api") version bmuschkoVersion apply false

        // ktor
        id("io.ktor.plugin") version ktorVersion apply false

        // shadow
        id("com.github.johnrengelman.shadow") version pluginShadow apply false
    }
}

include("acceptance")
include("common")
include("api-jackson")
include("api-multiplatform")
include("mappers-jackson")
include("mappers-multiplatform")
include("app-spring")
include("business")
include("stubs")
include("app-ktor")
include("app-serverless")
include("app-rabbitmq")
include("app-kafka")
include("lib-logging-common")
include("lib-logging-logback")
include("lib-logging-kermit")
include("api-log1")
include("mappers-log1")
include("lib-cor")
