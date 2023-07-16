package me.neversleeps.app.multiplatform

import io.ktor.server.application.* // ktlint-disable no-wildcard-imports
import io.ktor.server.request.*
import io.ktor.server.response.*
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import me.neversleeps.api.multiplatform.apiMapper
import me.neversleeps.api.multiplatform.v1.models.ProjectCreateRequest
import me.neversleeps.api.multiplatform.v1.models.ProjectDeleteRequest
import me.neversleeps.api.multiplatform.v1.models.ProjectReadRequest
import me.neversleeps.api.multiplatform.v1.models.ProjectSearchRequest
import me.neversleeps.api.multiplatform.v1.models.ProjectUpdateRequest
import me.neversleeps.app.AppSettings
import me.neversleeps.common.ProjectContext
import me.neversleeps.mappers.multiplatform.fromInternal.toTransportCreate
import me.neversleeps.mappers.multiplatform.fromInternal.toTransportDelete
import me.neversleeps.mappers.multiplatform.fromInternal.toTransportRead
import me.neversleeps.mappers.multiplatform.fromInternal.toTransportSearch
import me.neversleeps.mappers.multiplatform.fromInternal.toTransportUpdate
import me.neversleeps.mappers.multiplatform.fromTransport.fromTransport

suspend fun ApplicationCall.createProject(appSettings: AppSettings) {
    val processor = appSettings.projectProcessor
    val request = apiMapper.decodeFromString<ProjectCreateRequest>(receiveText())
    val context = ProjectContext()
    context.fromTransport(request)
    processor.execute(context)
    respond(apiMapper.encodeToString(context.toTransportCreate()))
}

suspend fun ApplicationCall.readProject(appSettings: AppSettings) {
    val processor = appSettings.projectProcessor
    val request = apiMapper.decodeFromString<ProjectReadRequest>(receiveText())
    val context = ProjectContext()
    context.fromTransport(request)
    processor.execute(context)
    respond(apiMapper.encodeToString(context.toTransportRead()))
}

suspend fun ApplicationCall.updateProject(appSettings: AppSettings) {
    val processor = appSettings.projectProcessor
    val request = apiMapper.decodeFromString<ProjectUpdateRequest>(receiveText())
    val context = ProjectContext()
    context.fromTransport(request)
    processor.execute(context)
    respond(apiMapper.encodeToString(context.toTransportUpdate()))
}

suspend fun ApplicationCall.deleteProject(appSettings: AppSettings) {
    val processor = appSettings.projectProcessor
    val request = apiMapper.decodeFromString<ProjectDeleteRequest>(receiveText())
    val context = ProjectContext()
    context.fromTransport(request)
    processor.execute(context)
    respond(apiMapper.encodeToString(context.toTransportDelete()))
}

suspend fun ApplicationCall.searchProject(appSettings: AppSettings) {
    val processor = appSettings.projectProcessor
    val request = apiMapper.decodeFromString<ProjectSearchRequest>(receiveText())
    val context = ProjectContext()
    context.fromTransport(request)
    processor.execute(context)
    respond(apiMapper.encodeToString(context.toTransportSearch()))
}
