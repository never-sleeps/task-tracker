package fromTransport

import TaskContext
import UnknownRequestClass
import me.neversleeps.api.multiplatform.v1.models.IRequest
import me.neversleeps.api.multiplatform.v1.models.TaskCreateRequest
import me.neversleeps.api.multiplatform.v1.models.TaskDeleteRequest
import me.neversleeps.api.multiplatform.v1.models.TaskReadRequest
import me.neversleeps.api.multiplatform.v1.models.TaskSearchRequest
import me.neversleeps.api.multiplatform.v1.models.TaskUpdateRequest
import models.AppCommand
import models.task.Task
import models.task.TaskSearchFilter

fun TaskContext.fromTransport(request: IRequest) = when (request) {
    is TaskCreateRequest -> fromTransport(request)
    is TaskReadRequest -> fromTransport(request)
    is TaskUpdateRequest -> fromTransport(request)
    is TaskDeleteRequest -> fromTransport(request)
    is TaskSearchRequest -> fromTransport(request)
    else -> throw UnknownRequestClass(request::class)
}

fun TaskContext.fromTransport(request: TaskCreateRequest) {
    requestId = request.toRequestId()
    command = AppCommand.CREATE
    taskRequest = request.data?.toInternal() ?: Task()
    stubCase = request.stub?.let { it.toInternal() } ?: stubs.TaskDebugStub.NONE
}

fun TaskContext.fromTransport(request: TaskReadRequest) {
    requestId = request.toRequestId()
    command = AppCommand.READ
    taskRequest = request.id.toTaskWithId()
    stubCase = request.stub?.let { it.toInternal() } ?: stubs.TaskDebugStub.NONE
}

fun TaskContext.fromTransport(request: TaskUpdateRequest) {
    requestId = request.toRequestId()
    command = AppCommand.UPDATE
    taskRequest = request.data?.toInternal() ?: Task()
    stubCase = request.stub?.let { it.toInternal() } ?: stubs.TaskDebugStub.NONE
}

fun TaskContext.fromTransport(request: TaskDeleteRequest) {
    requestId = request.toRequestId()
    command = AppCommand.DELETE
    taskRequest = request.id.toTaskWithId()
    stubCase = request.stub?.let { it.toInternal() } ?: stubs.TaskDebugStub.NONE
}

fun TaskContext.fromTransport(request: TaskSearchRequest) {
    requestId = request.toRequestId()
    command = AppCommand.DELETE
    taskSearchFilterRequest = request.filter?.toInternal() ?: TaskSearchFilter()
    stubCase = request.stub?.let { it.toInternal() } ?: stubs.TaskDebugStub.NONE
}
