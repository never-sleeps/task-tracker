package me.neversleeps.common

import kotlinx.datetime.Instant
import me.neversleeps.common.models.AppCommand
import me.neversleeps.common.models.AppError
import me.neversleeps.common.models.AppState
import me.neversleeps.common.models.RequestId
import me.neversleeps.common.models.task.Task
import me.neversleeps.common.models.task.TaskSearchFilter
import me.neversleeps.common.stubs.TaskDebugStub
import me.neversleeps.common.stubs.WorkMode

data class TaskContext(
    override var requestId: RequestId = RequestId.NONE,
    override var timeStart: Instant = Instant.NONE,
    override var command: AppCommand = AppCommand.NONE,
    override var state: AppState = AppState.NONE,
    override var errors: MutableList<AppError> = mutableListOf(),
    var settings: CorSettings = CorSettings.NONE,

    var taskRequest: Task = Task(),
    var taskSearchFilterRequest: TaskSearchFilter = TaskSearchFilter(),
    var taskResponse: Task = Task(),
    var tasksResponse: MutableList<Task> = mutableListOf(),
    var stubCase: TaskDebugStub = TaskDebugStub.NONE,
    var workMode: WorkMode = WorkMode.PROD,
) : IContext
