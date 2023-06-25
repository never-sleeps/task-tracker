package me.neversleeps.app.multiplatform

import io.ktor.server.application.* // ktlint-disable no-wildcard-imports
import io.ktor.server.request.*
import io.ktor.server.response.*
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import me.neversleeps.api.multiplatform.apiMapper
import me.neversleeps.api.multiplatform.v1.models.TaskCreateRequest
import me.neversleeps.api.multiplatform.v1.models.TaskDeleteRequest
import me.neversleeps.api.multiplatform.v1.models.TaskReadRequest
import me.neversleeps.api.multiplatform.v1.models.TaskSearchRequest
import me.neversleeps.api.multiplatform.v1.models.TaskUpdateRequest
import me.neversleeps.business.TaskProcessor
import me.neversleeps.common.TaskContext
import me.neversleeps.mappers.multiplatform.fromInternal.toTransportCreate
import me.neversleeps.mappers.multiplatform.fromInternal.toTransportDelete
import me.neversleeps.mappers.multiplatform.fromInternal.toTransportRead
import me.neversleeps.mappers.multiplatform.fromInternal.toTransportSearch
import me.neversleeps.mappers.multiplatform.fromInternal.toTransportUpdate
import me.neversleeps.mappers.multiplatform.fromTransport.fromTransport

suspend fun ApplicationCall.create(processor: TaskProcessor) {
    val request = apiMapper.decodeFromString<TaskCreateRequest>(receiveText())
    val context = TaskContext()
    context.fromTransport(request)
    processor.execute(context)
    respond(apiMapper.encodeToString(context.toTransportCreate()))
}

suspend fun ApplicationCall.read(processor: TaskProcessor) {
    val request = apiMapper.decodeFromString<TaskReadRequest>(receiveText())
    val context = TaskContext()
    context.fromTransport(request)
    processor.execute(context)
    respond(apiMapper.encodeToString(context.toTransportRead()))
}

suspend fun ApplicationCall.update(processor: TaskProcessor) {
    val request = apiMapper.decodeFromString<TaskUpdateRequest>(receiveText())
    val context = TaskContext()
    context.fromTransport(request)
    processor.execute(context)
    respond(apiMapper.encodeToString(context.toTransportUpdate()))
}

suspend fun ApplicationCall.delete(processor: TaskProcessor) {
    val request = apiMapper.decodeFromString<TaskDeleteRequest>(receiveText())
    val context = TaskContext()
    context.fromTransport(request)
    processor.execute(context)
    respond(apiMapper.encodeToString(context.toTransportDelete()))
}

suspend fun ApplicationCall.search(processor: TaskProcessor) {
    val request = apiMapper.decodeFromString<TaskSearchRequest>(receiveText())
    val context = TaskContext()
    context.fromTransport(request)
    processor.execute(context)
    respond(apiMapper.encodeToString(context.toTransportSearch()))
}
