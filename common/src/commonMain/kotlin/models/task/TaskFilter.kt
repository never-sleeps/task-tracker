package ru.otus.otuskotlin.marketplace.common.models.task

import kotlinx.datetime.Instant
import ru.otus.otuskotlin.marketplace.common.NONE
import ru.otus.otuskotlin.marketplace.common.models.user.UserId

data class TaskFilter(
    var searchText: String = "",
    var createdBy: UserId = UserId.NONE,
    var type: TaskType = TaskType.NONE,
    var executor: UserId = UserId.NONE,
    var dueDate: Instant = Instant.NONE,
)
