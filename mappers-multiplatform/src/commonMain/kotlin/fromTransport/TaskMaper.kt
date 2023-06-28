package me.neversleeps.mappers.multiplatform.fromTransport

import me.neversleeps.api.multiplatform.v1.models.TaskCreateObject
import me.neversleeps.api.multiplatform.v1.models.TaskDebugStub
import me.neversleeps.api.multiplatform.v1.models.TaskPriority
import me.neversleeps.api.multiplatform.v1.models.TaskSearchFilter
import me.neversleeps.api.multiplatform.v1.models.TaskStatus
import me.neversleeps.api.multiplatform.v1.models.TaskType
import me.neversleeps.api.multiplatform.v1.models.TaskUpdateObject
import me.neversleeps.common.models.task.Task
import me.neversleeps.common.models.task.TaskId

fun TaskCreateObject.toInternal(): Task = Task(
    type = this.type.toInternal(),
    priority = this.priority.toInternal(),
    status = this.status.toInternal(),
    title = this.title ?: "",
    description = this.description ?: "",
    executor = this.executor.toUserId(),
    createdBy = this.createdBy.toUserId(),
)

fun TaskUpdateObject.toInternal(): Task = Task(
    id = this.id.toTaskId(),
    type = this.type.toInternal(),
    priority = this.priority.toInternal(),
    status = this.status.toInternal(),
    title = this.title ?: "",
    description = this.description ?: "",
    executor = this.executor.toUserId(),
    createdBy = this.createdBy.toUserId(),
)

fun TaskType?.toInternal(): me.neversleeps.common.models.task.TaskType = when (this) {
    TaskType.BACKEND -> me.neversleeps.common.models.task.TaskType.BACKEND
    TaskType.FRONTEND -> me.neversleeps.common.models.task.TaskType.FRONTEND
    TaskType.DESIGN -> me.neversleeps.common.models.task.TaskType.DESIGN
    TaskType.TESTING -> me.neversleeps.common.models.task.TaskType.TESTING
    TaskType.OTHER -> me.neversleeps.common.models.task.TaskType.OTHER
    null -> me.neversleeps.common.models.task.TaskType.NONE
}

fun TaskPriority?.toInternal(): me.neversleeps.common.models.task.TaskPriority = when (this) {
    TaskPriority.CRITICAL -> me.neversleeps.common.models.task.TaskPriority.CRITICAL
    TaskPriority.HIGH -> me.neversleeps.common.models.task.TaskPriority.HIGH
    TaskPriority.MEDIUM -> me.neversleeps.common.models.task.TaskPriority.MEDIUM
    TaskPriority.LOW -> me.neversleeps.common.models.task.TaskPriority.LOW
    else -> me.neversleeps.common.models.task.TaskPriority.NONE
}

fun TaskStatus?.toInternal(): me.neversleeps.common.models.task.TaskStatus = when (this) {
    TaskStatus.TODO -> me.neversleeps.common.models.task.TaskStatus.TODO
    TaskStatus.IN_PROGRESS -> me.neversleeps.common.models.task.TaskStatus.IN_PROGRESS
    TaskStatus.DONE -> me.neversleeps.common.models.task.TaskStatus.DONE
    TaskStatus.CANCELED -> me.neversleeps.common.models.task.TaskStatus.CANCELED
    else -> me.neversleeps.common.models.task.TaskStatus.NONE
}

fun TaskDebugStub?.toInternal(): me.neversleeps.common.stubs.TaskDebugStub = when (this) {
    TaskDebugStub.SUCCESS -> me.neversleeps.common.stubs.TaskDebugStub.SUCCESS
    TaskDebugStub.NOT_FOUND -> me.neversleeps.common.stubs.TaskDebugStub.NOT_FOUND
    TaskDebugStub.BAD_ID -> me.neversleeps.common.stubs.TaskDebugStub.BAD_ID
    TaskDebugStub.BAD_TITLE -> me.neversleeps.common.stubs.TaskDebugStub.BAD_TITLE
    TaskDebugStub.BAD_SEARCH_EXECUTOR -> me.neversleeps.common.stubs.TaskDebugStub.BAD_SEARCH_EXECUTOR
    TaskDebugStub.BAD_SEARCHСREATED_BY -> me.neversleeps.common.stubs.TaskDebugStub.BAD_SEARCH_CREATED_BY
    TaskDebugStub.PERMISSION_ERROR -> me.neversleeps.common.stubs.TaskDebugStub.PERMISSION_ERROR
    null -> me.neversleeps.common.stubs.TaskDebugStub.NONE
}

fun TaskSearchFilter.toInternal(): me.neversleeps.common.models.task.TaskSearchFilter =
    me.neversleeps.common.models.task.TaskSearchFilter(
        searchText = this.searchText ?: "",
        createdBy = this.createdBy.toUserId(),
        type = this.type?.toInternal() ?: me.neversleeps.common.models.task.TaskType.NONE,
        executor = this.createdBy.toUserId(),
    )

fun String?.toTaskWithId() = Task(id = this.toTaskId())

fun String?.toTaskId() = this?.let { TaskId(it) } ?: TaskId.NONE
