package me.neversleeps.mappers.jackson.fromInternal

import me.neversleeps.api.jackson.v1.models.TaskPermission
import me.neversleeps.api.jackson.v1.models.TaskPriority
import me.neversleeps.api.jackson.v1.models.TaskResponseObject
import me.neversleeps.api.jackson.v1.models.TaskStatus
import me.neversleeps.api.jackson.v1.models.TaskType
import me.neversleeps.common.models.task.Task

fun List<Task>.toTransport(): List<TaskResponseObject> =
    this.map { it.toTransport() }

fun Task.toTransport(): TaskResponseObject = TaskResponseObject(
    id = this.id.asString(),
    type = this.type.toTransport(),
    priority = this.priority.toTransport(),
    status = this.status.toTransport(),
    title = this.title,
    description = this.description,
    executor = this.executor.asString(),
    createdBy = this.createdBy.asString(),
    permissions = this.permissions.toTransport(),
)

private fun me.neversleeps.common.models.task.TaskType.toTransport(): TaskType? = when (this) {
    me.neversleeps.common.models.task.TaskType.BACKEND -> TaskType.BACKEND
    me.neversleeps.common.models.task.TaskType.FRONTEND -> TaskType.FRONTEND
    me.neversleeps.common.models.task.TaskType.DESIGN -> TaskType.DESIGN
    me.neversleeps.common.models.task.TaskType.TESTING -> TaskType.TESTING
    me.neversleeps.common.models.task.TaskType.OTHER -> TaskType.OTHER
    me.neversleeps.common.models.task.TaskType.NONE -> null
}

private fun me.neversleeps.common.models.task.TaskPriority.toTransport(): TaskPriority? = when (this) {
    me.neversleeps.common.models.task.TaskPriority.CRITICAL -> TaskPriority.CRITICAL
    me.neversleeps.common.models.task.TaskPriority.HIGH -> TaskPriority.HIGH
    me.neversleeps.common.models.task.TaskPriority.MEDIUM -> TaskPriority.MEDIUM
    me.neversleeps.common.models.task.TaskPriority.LOW -> TaskPriority.LOW
    me.neversleeps.common.models.task.TaskPriority.NONE -> null
}

private fun me.neversleeps.common.models.task.TaskStatus.toTransport(): TaskStatus? = when (this) {
    me.neversleeps.common.models.task.TaskStatus.TODO -> TaskStatus.TODO
    me.neversleeps.common.models.task.TaskStatus.IN_PROGRESS -> TaskStatus.IN_PROGRESS
    me.neversleeps.common.models.task.TaskStatus.DONE -> TaskStatus.DONE
    me.neversleeps.common.models.task.TaskStatus.CANCELED -> TaskStatus.CANCELED
    me.neversleeps.common.models.task.TaskStatus.NONE -> null
}

fun Set<me.neversleeps.common.models.task.TaskPermission>.toTransport(): Set<TaskPermission> = this
    .map { it.toTransport() }
    .toSet()

fun me.neversleeps.common.models.task.TaskPermission.toTransport(): TaskPermission = when (this) {
    me.neversleeps.common.models.task.TaskPermission.READ -> TaskPermission.READ
    me.neversleeps.common.models.task.TaskPermission.UPDATE -> TaskPermission.UPDATE
    me.neversleeps.common.models.task.TaskPermission.DELETE -> TaskPermission.DELETE
}
