package me.neversleeps.app

import io.ktor.http.* // ktlint-disable no-wildcard-imports
import io.ktor.serialization.jackson.jackson
import io.ktor.server.application.*
import io.ktor.server.http.content.resources
import io.ktor.server.http.content.static
import io.ktor.server.plugins.contentnegotiation.ContentNegotiation
import io.ktor.server.routing.*
import io.ktor.server.websocket.*
import io.ktor.util.*
import me.neversleeps.api.jackson.apiMapper
import me.neversleeps.app.jackson.WsProjectControllerV1
import me.neversleeps.app.jackson.WsTaskControllerV1
import me.neversleeps.app.jackson.project
import me.neversleeps.app.jackson.task
import me.neversleeps.app.plugins.initAppSettings
import me.neversleeps.app.simpleWS.wsChat
import me.neversleeps.app.simpleWS.wsPing
import me.neversleeps.app.module as commonModule

// function with config (application.conf)
fun main(args: Array<String>): Unit = io.ktor.server.cio.EngineMain.main(args)

private val clazz = Application::moduleJvm::class.qualifiedName ?: "Application"

@Suppress("unused") // Referenced in application.conf
fun Application.moduleJvm(appSettings: AppSettings = initAppSettings()) {
    commonModule(appSettings)

    val wsProjectHandler = WsProjectControllerV1()
    val wsTaskHandler = WsTaskControllerV1()

    routing {
        route("api/v1") {
            pluginRegistry.getOrNull(AttributeKey("ContentNegotiation")) ?: install(ContentNegotiation) {
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
            wsProjectHandler.handle(this, appSettings)
        }
        webSocket("/ws/v1/task") {
            wsTaskHandler.handle(this, appSettings)
        }

        static("static") {
            resources("static")
        }
    }
}
