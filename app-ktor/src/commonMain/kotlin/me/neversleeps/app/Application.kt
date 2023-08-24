package me.neversleeps.app

import io.ktor.serialization.kotlinx.json.* // ktlint-disable no-wildcard-imports
import io.ktor.server.application.*
import io.ktor.server.cio.*
import io.ktor.server.engine.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.websocket.*
import io.ktor.util.*
import me.neversleeps.api.multiplatform.apiMapper
import me.neversleeps.app.multiplatform.WsProjectControllerV2
import me.neversleeps.app.multiplatform.WsTaskControllerV2
import me.neversleeps.app.multiplatform.project
import me.neversleeps.app.multiplatform.task
import me.neversleeps.app.plugins.initAppSettings
import me.neversleeps.app.plugins.initPlugins

fun Application.module(
    appSettings: AppSettings = initAppSettings(),
    installPlugins: Boolean = true,
) {
    initPlugins(appSettings)

    val wsProjectHandler = WsProjectControllerV2()
    val wsTaskHandler = WsTaskControllerV2()

    routing {
        get("/") {
            call.respondText("Hello, world!")
        }
        route("/api/v2") { // до этого разделения на v1/v2 не было. тут введено из-за io.ktor.server.application.DuplicatePluginException
            pluginRegistry.getOrNull(AttributeKey("ContentNegotiation")) ?: install(ContentNegotiation) {
                json(apiMapper)
            }

            project(appSettings)
            task(appSettings)
        }
        webSocket("/ws/v2/project") {
            wsProjectHandler.handle(this, appSettings)
        }
        webSocket("/ws/v2/task") {
            wsTaskHandler.handle(this, appSettings)
        }
    }
}

fun main() {
    embeddedServer(CIO, port = 8080, module = Application::module).start(wait = true)
}
