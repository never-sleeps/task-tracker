package me.neversleeps.app

import io.ktor.serialization.kotlinx.json.* // ktlint-disable no-wildcard-imports
import io.ktor.server.application.*
import io.ktor.server.cio.*
import io.ktor.server.engine.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.websocket.*
import me.neversleeps.api.multiplatform.apiMapper
import me.neversleeps.app.multiplatform.project
import me.neversleeps.app.multiplatform.task
import me.neversleeps.app.multiplatform.wsHandlerV2
import me.neversleeps.business.ProjectProcessor
import me.neversleeps.business.TaskProcessor

fun Application.module(
    projectProcessor: ProjectProcessor = ProjectProcessor(),
    taskProcessor: TaskProcessor = TaskProcessor(),
    installPlugins: Boolean = true
) {
    if (installPlugins) {
        install(WebSockets)
    }

    routing {
        get("/") {
            call.respondText("Hello, world!")
        }
        route("/api/v2") { // до этого разделения на v1/v2 не было. тут введено из-за io.ktor.server.application.DuplicatePluginException
            install(ContentNegotiation) {
                json(apiMapper)
            }

            project(projectProcessor)
            task(taskProcessor)
        }
        webSocket("/ws/v2/project") {
            wsHandlerV2(projectProcessor)
        }
        webSocket("/ws/v2/task") {
            wsHandlerV2(taskProcessor)
        }
    }
}

fun main() {
    embeddedServer(CIO, port = 8080, module = Application::module).start(wait = true)
}
