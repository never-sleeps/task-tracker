package me.neversleeps.app.jackson

import io.ktor.server.application.* // ktlint-disable no-wildcard-imports
import io.ktor.server.request.*
import io.ktor.server.response.*
import me.neversleeps.api.jackson.v1.models.ProjectCreateRequest
import me.neversleeps.api.jackson.v1.models.ProjectDeleteRequest
import me.neversleeps.api.jackson.v1.models.ProjectReadRequest
import me.neversleeps.api.jackson.v1.models.ProjectSearchRequest
import me.neversleeps.api.jackson.v1.models.ProjectUpdateRequest
import me.neversleeps.app.AppSettings
import me.neversleeps.app.multiplatform.createProject
import me.neversleeps.common.ProjectContext
import me.neversleeps.mappers.jackson.fromInternal.toTransportCreate
import me.neversleeps.mappers.jackson.fromInternal.toTransportDelete
import me.neversleeps.mappers.jackson.fromInternal.toTransportRead
import me.neversleeps.mappers.jackson.fromInternal.toTransportSearch
import me.neversleeps.mappers.jackson.fromInternal.toTransportUpdate
import me.neversleeps.mappers.jackson.fromTransport.fromTransport
import me.neversleeps.mappers.log1.toLog

private val clazzCreate = ApplicationCall::createProject::class.qualifiedName ?: "createProject"

suspend fun ApplicationCall.createProject(appSettings: AppSettings) {
    val logId = "createProject"
    val logger = appSettings.corSettings.loggerProvider.logger(clazzCreate)

    logger.doWithLogging(logId) {
        val processor = appSettings.projectProcessor
        val request = receive<ProjectCreateRequest>() // получаем некий gson
        val context = ProjectContext().apply { this.fromTransport(request) } // маппим его из транспортной модели и закидываем это в контекст

        logger.info(
            msg = "${context.command} request is got",
            data = context.toLog("$logId-request"),
        )

        processor.execute(context) // контекст передаём в бизнес-логику
        respond(context.toTransportCreate()) // бизнес-логику конвертируем в транспортную модель и передаём обратно во framework

        logger.info(
            msg = "${context.command} response is sent",
            data = context.toLog("$logId-response"),
        )
    }
}

suspend fun ApplicationCall.readProject(appSettings: AppSettings) {
    val processor = appSettings.projectProcessor
    val request = receive<ProjectReadRequest>()
    val context = ProjectContext().apply { this.fromTransport(request) }
    processor.execute(context)
    respond(context.toTransportRead())
}

suspend fun ApplicationCall.updateProject(appSettings: AppSettings) {
    val processor = appSettings.projectProcessor
    val request = receive<ProjectUpdateRequest>()
    val context = ProjectContext().apply { this.fromTransport(request) }
    processor.execute(context)
    respond(context.toTransportUpdate())
}

suspend fun ApplicationCall.deleteProject(appSettings: AppSettings) {
    val processor = appSettings.projectProcessor
    val request = receive<ProjectDeleteRequest>()
    val context = ProjectContext().apply { this.fromTransport(request) }
    processor.execute(context)
    respond(context.toTransportDelete())
}

suspend fun ApplicationCall.searchProject(appSettings: AppSettings) {
    val processor = appSettings.projectProcessor
    val request = receive<ProjectSearchRequest>()
    val context = ProjectContext().apply { this.fromTransport(request) }
    processor.execute(context)
    respond(context.toTransportSearch())
}
