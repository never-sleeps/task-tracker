package me.neversleeps.app.jackson

import io.ktor.server.application.* // ktlint-disable no-wildcard-imports
import io.ktor.server.request.*
import io.ktor.server.response.*
import me.neversleeps.api.jackson.v1.models.ProjectCreateRequest
import me.neversleeps.api.jackson.v1.models.ProjectDeleteRequest
import me.neversleeps.api.jackson.v1.models.ProjectReadRequest
import me.neversleeps.api.jackson.v1.models.ProjectSearchRequest
import me.neversleeps.api.jackson.v1.models.ProjectUpdateRequest
import me.neversleeps.business.ProjectProcessor
import me.neversleeps.common.ProjectContext
import me.neversleeps.mappers.jackson.fromInternal.toTransportCreate
import me.neversleeps.mappers.jackson.fromInternal.toTransportDelete
import me.neversleeps.mappers.jackson.fromInternal.toTransportRead
import me.neversleeps.mappers.jackson.fromInternal.toTransportSearch
import me.neversleeps.mappers.jackson.fromInternal.toTransportUpdate
import me.neversleeps.mappers.jackson.fromTransport.fromTransport

suspend fun ApplicationCall.create(processor: ProjectProcessor) {
    val request = receive<ProjectCreateRequest>() // получаем некий gson
    val context = ProjectContext().apply { this.fromTransport(request) } // маппим его из транспортной модели и закидываем это в контекст
    processor.execute(context) // контекст передаём в бизнес-логику
    respond(context.toTransportCreate()) // бизнес-логику конвертируем в транспортную модель и передаём обратно во framework
}

suspend fun ApplicationCall.read(processor: ProjectProcessor) {
    val request = receive<ProjectReadRequest>()
    val context = ProjectContext().apply { this.fromTransport(request) }
    processor.execute(context)
    respond(context.toTransportRead())
}

suspend fun ApplicationCall.update(processor: ProjectProcessor) {
    val request = receive<ProjectUpdateRequest>()
    val context = ProjectContext().apply { this.fromTransport(request) }
    processor.execute(context)
    respond(context.toTransportUpdate())
}

suspend fun ApplicationCall.delete(processor: ProjectProcessor) {
    val request = receive<ProjectDeleteRequest>()
    val context = ProjectContext().apply { this.fromTransport(request) }
    processor.execute(context)
    respond(context.toTransportDelete())
}

suspend fun ApplicationCall.search(processor: ProjectProcessor) {
    val request = receive<ProjectSearchRequest>()
    val context = ProjectContext().apply { this.fromTransport(request) }
    processor.execute(context)
    respond(context.toTransportSearch())
}
