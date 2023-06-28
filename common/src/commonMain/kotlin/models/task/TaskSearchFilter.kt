package me.neversleeps.common.models.task

import me.neversleeps.common.models.user.UserId

data class TaskSearchFilter(
    var searchText: String = "",
    var createdBy: UserId = UserId.NONE,
    var type: TaskType = TaskType.NONE,
    var executor: UserId = UserId.NONE,
)
