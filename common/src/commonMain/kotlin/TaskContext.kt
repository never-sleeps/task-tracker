package ru.otus.otuskotlin.marketplace.common

import kotlinx.datetime.Instant
import ru.otus.otuskotlin.marketplace.common.models.AppCommand
import ru.otus.otuskotlin.marketplace.common.models.AppError
import ru.otus.otuskotlin.marketplace.common.models.AppState
import ru.otus.otuskotlin.marketplace.common.models.RequestId
import ru.otus.otuskotlin.marketplace.common.models.task.Task
import ru.otus.otuskotlin.marketplace.common.models.task.TaskFilter
import ru.otus.otuskotlin.marketplace.common.stubs.TaskStub

data class TaskContext(
    override var requestId: RequestId = RequestId.NONE,
    override var timeStart: Instant = Instant.NONE,
    override var command: AppCommand = AppCommand.NONE,
    override var state: AppState = AppState.NONE,
    override var errors: MutableList<AppError> = mutableListOf(),

    var taskRequest: Task = Task(),
    var taskFilterRequest: TaskFilter = TaskFilter(),
    var taskResponse: Task = Task(),
    var tasksResponse: MutableList<Task> = mutableListOf(),
    var stubCase: TaskStub = TaskStub.NONE,
) : IContext
