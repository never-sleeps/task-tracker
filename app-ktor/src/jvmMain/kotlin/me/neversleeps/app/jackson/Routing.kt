package me.neversleeps.app.jackson // ktlint-disable filename

import io.ktor.server.application.* // ktlint-disable no-wildcard-imports
import io.ktor.server.routing.*
import me.neversleeps.app.AppSettings

fun Route.project(appSettings: AppSettings) {
    val logger = appSettings.corSettings.loggerProvider.logger(Route::project::class) // with logging
    route("project") {
        post("create") {
            call.createProject(appSettings, logger)
        }
        post("read") {
            call.readProject(appSettings, logger)
        }
        post("update") {
            call.updateProject(appSettings, logger)
        }
        post("delete") {
            call.deleteProject(appSettings, logger)
        }
        post("search") {
            call.searchProject(appSettings, logger)
        }
    }
}

fun Route.task(appSettings: AppSettings) {
    route("task") { // without logging
        post("create") {
            call.createTask(appSettings)
        }
        post("read") {
            call.readTask(appSettings)
        }
        post("update") {
            call.updateTask(appSettings)
        }
        post("delete") {
            call.deleteTask(appSettings)
        }
        post("search") {
            call.searchTask(appSettings)
        }
    }
}
