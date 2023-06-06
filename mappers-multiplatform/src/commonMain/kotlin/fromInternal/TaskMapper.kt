package fromInternal

import me.neversleeps.api.multiplatform.v1.models.TaskPermission
import me.neversleeps.api.multiplatform.v1.models.TaskPriority
import me.neversleeps.api.multiplatform.v1.models.TaskResponseObject
import me.neversleeps.api.multiplatform.v1.models.TaskStatus
import me.neversleeps.api.multiplatform.v1.models.TaskType
import models.task.Task

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

private fun models.task.TaskType.toTransport(): TaskType? = when (this) {
    models.task.TaskType.BACKEND -> TaskType.BACKEND
    models.task.TaskType.FRONTEND -> TaskType.FRONTEND
    models.task.TaskType.DESIGN -> TaskType.DESIGN
    models.task.TaskType.TESTING -> TaskType.TESTING
    models.task.TaskType.OTHER -> TaskType.OTHER
    models.task.TaskType.NONE -> null
}

private fun models.task.TaskPriority.toTransport(): TaskPriority? = when (this) {
    models.task.TaskPriority.CRITICAL -> TaskPriority.CRITICAL
    models.task.TaskPriority.HIGH -> TaskPriority.HIGH
    models.task.TaskPriority.MEDIUM -> TaskPriority.MEDIUM
    models.task.TaskPriority.LOW -> TaskPriority.LOW
    models.task.TaskPriority.NONE -> null
}

private fun models.task.TaskStatus.toTransport(): TaskStatus? = when (this) {
    models.task.TaskStatus.TODO -> TaskStatus.TODO
    models.task.TaskStatus.IN_PROGRESS -> TaskStatus.IN_PROGRESS
    models.task.TaskStatus.DONE -> TaskStatus.DONE
    models.task.TaskStatus.CANCELED -> TaskStatus.CANCELED
    models.task.TaskStatus.NONE -> null
}

fun Set<models.task.TaskPermission>.toTransport(): Set<TaskPermission> = this
    .map { it.toTransport() }
    .toSet()

fun models.task.TaskPermission.toTransport(): TaskPermission = when (this) {
    models.task.TaskPermission.READ -> TaskPermission.READ
    models.task.TaskPermission.UPDATE -> TaskPermission.UPDATE
    models.task.TaskPermission.DELETE -> TaskPermission.DELETE
}
