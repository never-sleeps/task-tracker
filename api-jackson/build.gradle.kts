plugins {
    kotlin("jvm")
    id("org.openapi.generator")
}

dependencies {
    val jacksonVersion: String by project
    implementation(kotlin("stdlib"))

    implementation("com.fasterxml.jackson.module:jackson-module-kotlin:$jacksonVersion")
    implementation("com.fasterxml.jackson.datatype:jackson-datatype-jsr310:$jacksonVersion")

    testImplementation(kotlin("test-junit"))
}

sourceSets {
    main {
        java.srcDir("$buildDir/generate-resources/main/src/main/kotlin")
    }
}

/**
 * Path to generated files: api-jackson/build/generate-resources/main/src/main/kotlin/me/neversleeps/api/jackson/v1/models
 */
openApiGenerate {
    val openapiGroup = "${rootProject.group}.api.jackson.v1"
    generatorName.set("kotlin") // активный генератор
    packageName.set(openapiGroup)
    apiPackage.set("$openapiGroup.api")
    modelPackage.set("$openapiGroup.models")
    invokerPackage.set("$openapiGroup.invoker")
    inputSpec.set("$rootDir/_specs/v1/specs.yaml")

    /**
     * https://openapi-generator.tech/docs/globals
     * Нам нужны только модели
     */
    globalProperties.apply {
        put("models", "")
        put("modelDocs", "false")
    }

    /**
     * Настройка доп.параметров из документации по генератору
     * https://github.com/OpenAPITools/openapi-generator/blob/master/docs/generators/kotlin.md
     */
    configOptions.set(
        mapOf(
            "serializationLibrary" to "jackson",
            "dateLibrary" to "string",
            "collectionType" to "list",
            "enumPropertyNaming" to "UPPERCASE",
        ),
    )
}

tasks {
    compileKotlin {
        dependsOn(openApiGenerate)
    }
}
