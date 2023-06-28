package me.neversleeps.app.jackson // ktlint-disable filename

import io.ktor.server.application.* // ktlint-disable no-wildcard-imports
import io.ktor.server.routing.*
import me.neversleeps.business.ProjectProcessor
import me.neversleeps.business.TaskProcessor

fun Route.project(processor: ProjectProcessor) {
    route("project") {
        post("create") {
            call.create(processor)
        }
        post("read") {
            call.read(processor)
        }
        post("update") {
            call.update(processor)
        }
        post("delete") {
            call.delete(processor)
        }
        post("search") {
            call.search(processor)
        }
    }
}

fun Route.task(processor: TaskProcessor) {
    route("task") {
        post("create") {
            call.create(processor)
        }
        post("read") {
            call.read(processor)
        }
        post("update") {
            call.update(processor)
        }
        post("delete") {
            call.delete(processor)
        }
        post("search") {
            call.search(processor)
        }
    }
}
