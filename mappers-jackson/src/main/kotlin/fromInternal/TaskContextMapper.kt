package me.neversleeps.mappers.jackson.fromInternal

import me.neversleeps.api.jackson.v1.models.IResponse
import me.neversleeps.api.jackson.v1.models.InitBaseResponse
import me.neversleeps.api.jackson.v1.models.ResponseResultStatus
import me.neversleeps.api.jackson.v1.models.TaskCreateResponse
import me.neversleeps.api.jackson.v1.models.TaskDeleteResponse
import me.neversleeps.api.jackson.v1.models.TaskReadResponse
import me.neversleeps.api.jackson.v1.models.TaskSearchResponse
import me.neversleeps.api.jackson.v1.models.TaskUpdateResponse
import me.neversleeps.common.TaskContext
import me.neversleeps.common.models.AppCommand
import me.neversleeps.mappers.jackson.UnknownCommandMapping

fun TaskContext.toTransport(): IResponse = when (val cmd = command) {
    AppCommand.CREATE -> toTransportCreate()
    AppCommand.READ -> toTransportRead()
    AppCommand.UPDATE -> toTransportUpdate()
    AppCommand.DELETE -> toTransportDelete()
    AppCommand.SEARCH -> toTransportSearch()
    AppCommand.NONE -> throw UnknownCommandMapping(cmd)
}

fun TaskContext.toTransportInit() = InitBaseResponse(
    requestId = this.requestId.asString(),
    resultStatus = if (errors.isEmpty()) ResponseResultStatus.SUCCESS else ResponseResultStatus.ERROR,
    errors = errors.toTransport(),
)

fun TaskContext.toTransportCreate() = TaskCreateResponse(
    responseType = "createTask",
    requestId = this.requestId.asString(),
    resultStatus = state.toTransport(),
    errors = errors.toTransport(),
    task = taskResponse.toTransport(),
)

fun TaskContext.toTransportRead() = TaskReadResponse(
    responseType = "readTask",
    requestId = this.requestId.asString(),
    resultStatus = state.toTransport(),
    errors = errors.toTransport(),
    task = taskResponse.toTransport(),
)

fun TaskContext.toTransportUpdate() = TaskUpdateResponse(
    responseType = "updateTask",
    requestId = this.requestId.asString(),
    resultStatus = state.toTransport(),
    errors = errors.toTransport(),
    task = taskResponse.toTransport(),
)

fun TaskContext.toTransportDelete() = TaskDeleteResponse(
    responseType = "deleteTask",
    requestId = this.requestId.asString(),
    resultStatus = state.toTransport(),
    errors = errors.toTransport(),
    task = taskResponse.toTransport(),
)

fun TaskContext.toTransportSearch() = TaskSearchResponse(
    responseType = "searchTask",
    requestId = this.requestId.asString(),
    resultStatus = state.toTransport(),
    errors = errors.toTransport(),
    tasks = tasksResponse.toTransport(),
)
