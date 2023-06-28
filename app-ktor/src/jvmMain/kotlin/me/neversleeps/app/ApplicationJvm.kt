package me.neversleeps.app

import io.ktor.http.* // ktlint-disable no-wildcard-imports
import io.ktor.serialization.jackson.jackson
import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.http.content.resources
import io.ktor.server.http.content.static
import io.ktor.server.locations.Locations
import io.ktor.server.plugins.autohead.AutoHeadResponse
import io.ktor.server.plugins.cachingheaders.CachingHeaders
import io.ktor.server.plugins.callloging.CallLogging
import io.ktor.server.plugins.contentnegotiation.ContentNegotiation
import io.ktor.server.plugins.cors.routing.CORS
import io.ktor.server.plugins.defaultheaders.DefaultHeaders
import io.ktor.server.routing.*
import me.neversleeps.api.jackson.apiMapper
import me.neversleeps.app.jackson.project
import me.neversleeps.app.jackson.task
import me.neversleeps.business.ProjectProcessor
import me.neversleeps.business.TaskProcessor
import org.slf4j.event.Level
import me.neversleeps.app.module as commonModule

// function with config (application.conf)
fun main(args: Array<String>): Unit = io.ktor.server.cio.EngineMain.main(args)

@Suppress("unused") // Referenced in application.conf
fun Application.moduleJvm() {
    val projectProcessor = ProjectProcessor()
    val taskProcessor = TaskProcessor()
    commonModule(projectProcessor, taskProcessor)

    install(CachingHeaders)
    install(DefaultHeaders)
    install(AutoHeadResponse)

    install(CORS) {
        allowMethod(HttpMethod.Options)
        allowMethod(HttpMethod.Put)
        allowMethod(HttpMethod.Delete)
        allowMethod(HttpMethod.Patch)
        allowHeader(HttpHeaders.Authorization)
        allowHeader("SomeCustomHeader")
        allowCredentials = true
        anyHost() // TODO remove
    }

    install(CallLogging) {
        level = Level.INFO
    }

    @Suppress("OPT_IN_USAGE")
    install(Locations)

    routing {
        route("v1") {
            install(ContentNegotiation) {
                jackson {
                    setConfig(apiMapper.serializationConfig)
                    setConfig(apiMapper.deserializationConfig)
                }
            }

            project(projectProcessor)
            task(taskProcessor)
        }

        static("static") {
            resources("static")
        }
    }
}
