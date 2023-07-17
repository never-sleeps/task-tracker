package me.neversleeps.app.jackson

import io.ktor.server.application.* // ktlint-disable no-wildcard-imports
import io.ktor.server.request.*
import io.ktor.server.response.*
import me.neversleeps.api.jackson.v1.models.TaskCreateRequest
import me.neversleeps.api.jackson.v1.models.TaskDeleteRequest
import me.neversleeps.api.jackson.v1.models.TaskReadRequest
import me.neversleeps.api.jackson.v1.models.TaskSearchRequest
import me.neversleeps.api.jackson.v1.models.TaskUpdateRequest
import me.neversleeps.app.AppSettings
import me.neversleeps.common.TaskContext
import me.neversleeps.mappers.jackson.fromInternal.toTransportCreate
import me.neversleeps.mappers.jackson.fromInternal.toTransportDelete
import me.neversleeps.mappers.jackson.fromInternal.toTransportRead
import me.neversleeps.mappers.jackson.fromInternal.toTransportSearch
import me.neversleeps.mappers.jackson.fromInternal.toTransportUpdate
import me.neversleeps.mappers.jackson.fromTransport.fromTransport

suspend fun ApplicationCall.createTask(appSettings: AppSettings) {
    val processor = appSettings.taskProcessor
    val request = receive<TaskCreateRequest>()
    val context = TaskContext().apply { this.fromTransport(request) }
    processor.execute(context)
    respond(context.toTransportCreate())
}

suspend fun ApplicationCall.readTask(appSettings: AppSettings) {
    val processor = appSettings.taskProcessor
    val request = receive<TaskReadRequest>()
    val context = TaskContext().apply { this.fromTransport(request) }
    processor.execute(context)
    respond(context.toTransportRead())
}

suspend fun ApplicationCall.updateTask(appSettings: AppSettings) {
    val processor = appSettings.taskProcessor
    val request = receive<TaskUpdateRequest>()
    val context = TaskContext().apply { this.fromTransport(request) }
    processor.execute(context)
    respond(context.toTransportUpdate())
}

suspend fun ApplicationCall.deleteTask(appSettings: AppSettings) {
    val processor = appSettings.taskProcessor
    val request = receive<TaskDeleteRequest>()
    val context = TaskContext().apply { this.fromTransport(request) }
    processor.execute(context)
    respond(context.toTransportDelete())
}

suspend fun ApplicationCall.searchTask(appSettings: AppSettings) {
    val processor = appSettings.taskProcessor
    val request = receive<TaskSearchRequest>()
    val context = TaskContext().apply { this.fromTransport(request) }
    processor.execute(context)
    respond(context.toTransportSearch())
}
