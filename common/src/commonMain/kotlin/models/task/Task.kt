package me.neversleeps.common.models.task

import me.neversleeps.common.models.user.UserId

data class Task(
    var id: TaskId = TaskId.NONE,
    var type: TaskType = TaskType.NONE,
    var priority: TaskPriority = TaskPriority.NONE,
    var status: TaskStatus = TaskStatus.NONE,
    var title: String = "",
    var description: String = "",
    var executor: UserId = UserId.NONE,
    var createdBy: UserId = UserId.NONE,
    var permissions: MutableSet<TaskPermission> = mutableSetOf(),
)

enum class TaskType {
    NONE,
    BACKEND,
    FRONTEND,
    DESIGN,
    TESTING,
    OTHER,
}

enum class TaskPriority {
    NONE,
    CRITICAL,
    HIGH,
    MEDIUM,
    LOW,
}

enum class TaskStatus {
    NONE,
    TODO,
    IN_PROGRESS,
    DONE,
    CANCELED,
}
