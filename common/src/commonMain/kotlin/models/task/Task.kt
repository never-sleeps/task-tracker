package ru.otus.otuskotlin.marketplace.common.models.task

import kotlinx.datetime.Instant
import ru.otus.otuskotlin.marketplace.common.NONE
import ru.otus.otuskotlin.marketplace.common.models.user.UserId

data class Task(
    var type: TaskType = TaskType.NONE,
    var priority: TaskPriority = TaskPriority.MEDIUM,
    var status: TaskStatus = TaskStatus.TODO,
    var title: String = "",
    var description: String = "",
    var executor: UserId = UserId.NONE,
    var dueDate: Instant = Instant.NONE,
    var createdBy: UserId = UserId.NONE
)

enum class TaskType {
    NONE,
    BACKEND,
    FRONTEND,
    DESIGN,
    TESTING,
    OTHER
}

enum class TaskPriority {
    CRITICAL,
    HIGH,
    MEDIUM,
    LOW
}

enum class TaskStatus {
    TODO,
    IN_PROGRESS,
    DONE,
    CANCELED
}
