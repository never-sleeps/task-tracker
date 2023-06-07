rootProject.name = "task-tracker"

pluginManagement {
    val kotlinVersion: String by settings
    val openapiVersion: String by settings

    plugins {
        kotlin("jvm") version kotlinVersion apply false
        kotlin("plugin.serialization") version kotlinVersion apply false

        id("org.openapi.generator") version openapiVersion apply false
    }
}

include("acceptance")
include("common")
include("api-jackson")
include("api-multiplatform")
include("mappers-jackson")
include("mappers-multiplatform")
