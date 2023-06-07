package fromTransport

import me.neversleeps.api.multiplatform.v1.models.TaskCreateObject
import me.neversleeps.api.multiplatform.v1.models.TaskDebugStub
import me.neversleeps.api.multiplatform.v1.models.TaskPriority
import me.neversleeps.api.multiplatform.v1.models.TaskSearchFilter
import me.neversleeps.api.multiplatform.v1.models.TaskStatus
import me.neversleeps.api.multiplatform.v1.models.TaskType
import me.neversleeps.api.multiplatform.v1.models.TaskUpdateObject
import models.task.Task
import models.task.TaskId

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

fun TaskType?.toInternal(): models.task.TaskType = when (this) {
    TaskType.BACKEND -> models.task.TaskType.BACKEND
    TaskType.FRONTEND -> models.task.TaskType.FRONTEND
    TaskType.DESIGN -> models.task.TaskType.DESIGN
    TaskType.TESTING -> models.task.TaskType.TESTING
    TaskType.OTHER -> models.task.TaskType.OTHER
    null -> models.task.TaskType.NONE
}

fun TaskPriority?.toInternal(): models.task.TaskPriority = when (this) {
    TaskPriority.CRITICAL -> models.task.TaskPriority.CRITICAL
    TaskPriority.HIGH -> models.task.TaskPriority.HIGH
    TaskPriority.MEDIUM -> models.task.TaskPriority.MEDIUM
    TaskPriority.LOW -> models.task.TaskPriority.LOW
    else -> models.task.TaskPriority.NONE
}

fun TaskStatus?.toInternal(): models.task.TaskStatus = when (this) {
    TaskStatus.TODO -> models.task.TaskStatus.TODO
    TaskStatus.IN_PROGRESS -> models.task.TaskStatus.IN_PROGRESS
    TaskStatus.DONE -> models.task.TaskStatus.DONE
    TaskStatus.CANCELED -> models.task.TaskStatus.CANCELED
    else -> models.task.TaskStatus.NONE
}

fun TaskDebugStub?.toInternal(): stubs.TaskDebugStub = when (this) {
    TaskDebugStub.SUCCESS -> stubs.TaskDebugStub.SUCCESS
    TaskDebugStub.NOT_FOUND -> stubs.TaskDebugStub.NOT_FOUND
    TaskDebugStub.BAD_ID -> stubs.TaskDebugStub.BAD_ID
    TaskDebugStub.BAD_TITLE -> stubs.TaskDebugStub.BAD_TITLE
    TaskDebugStub.BAD_SEARCH_EXECUTOR -> stubs.TaskDebugStub.BAD_SEARCH_EXECUTOR
    TaskDebugStub.BAD_SEARCHСREATED_BY -> stubs.TaskDebugStub.BAD_SEARCH_CREATED_BY
    TaskDebugStub.PERMISSION_ERROR -> stubs.TaskDebugStub.PERMISSION_ERROR
    null -> stubs.TaskDebugStub.NONE
}

fun TaskSearchFilter.toInternal(): models.task.TaskSearchFilter = models.task.TaskSearchFilter(
    searchText = this.searchText ?: "",
    createdBy = this.createdBy.toUserId(),
    type = this.type?.toInternal() ?: models.task.TaskType.NONE,
    executor = this.createdBy.toUserId(),
)

fun String?.toTaskWithId() = Task(id = this.toTaskId())

fun String?.toTaskId() = this?.let { TaskId(it) } ?: TaskId.NONE
