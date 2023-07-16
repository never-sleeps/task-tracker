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
import io.ktor.server.websocket.*
import me.neversleeps.api.jackson.apiMapper
import me.neversleeps.app.jackson.project
import me.neversleeps.app.jackson.task
import me.neversleeps.app.jackson.wsHandlerV1
import me.neversleeps.app.plugins.initAppSettings
import me.neversleeps.app.simpleWS.wsChat
import me.neversleeps.app.simpleWS.wsPing
import me.neversleeps.logging.jvm.LogWrapperLogback
import org.slf4j.event.Level
import me.neversleeps.app.module as commonModule

// function with config (application.conf)
fun main(args: Array<String>): Unit = io.ktor.server.cio.EngineMain.main(args)

private val clazz = Application::moduleJvm::class.qualifiedName ?: "Application"

@Suppress("unused") // Referenced in application.conf
fun Application.moduleJvm(appSettings: AppSettings = initAppSettings()) {
    commonModule(appSettings)

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
        val lgr = appSettings
            .corSettings
            .loggerProvider
            .logger(clazz) as? LogWrapperLogback
        lgr?.logger?.also { logger = it }
    }

    @Suppress("OPT_IN_USAGE")
    install(Locations)

    routing {
        route("api/v1") {
            install(ContentNegotiation) {
                jackson {
                    setConfig(apiMapper.serializationConfig)
                    setConfig(apiMapper.deserializationConfig)
                }
            }

            project(appSettings)
            task(appSettings)
        }

        webSocket("/ws/ping") {
            wsPing()
        }
        webSocket("/ws/chat") {
            wsChat()
        }

        webSocket("/ws/v1/project") {
            wsHandlerV1(appSettings.projectProcessor)
        }
        webSocket("/ws/v1/task") {
            wsHandlerV1(appSettings.taskProcessor)
        }

        static("static") {
            resources("static")
        }
    }
}
