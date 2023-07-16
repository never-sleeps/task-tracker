package me.neversleeps.app.jackson // ktlint-disable filename

import io.ktor.server.application.* // ktlint-disable no-wildcard-imports
import io.ktor.server.routing.*
import me.neversleeps.app.AppSettings

fun Route.project(appSettings: AppSettings) {
    route("project") {
        post("create") {
            call.createProject(appSettings)
        }
        post("read") {
            call.readProject(appSettings)
        }
        post("update") {
            call.updateProject(appSettings)
        }
        post("delete") {
            call.deleteProject(appSettings)
        }
        post("search") {
            call.searchProject(appSettings)
        }
    }
}

fun Route.task(appSettings: AppSettings) {
    route("task") {
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
