package me.neversleeps.business.workers

import me.neversleeps.common.TaskContext
import me.neversleeps.common.models.AppState
import me.neversleeps.common.models.task.TaskId
import me.neversleeps.common.models.task.TaskPriority
import me.neversleeps.common.models.task.TaskStatus
import me.neversleeps.common.models.task.TaskType
import me.neversleeps.common.models.user.UserId
import me.neversleeps.common.stubs.TaskDebugStub
import me.neversleeps.lib.cor.ICorChainDsl
import me.neversleeps.lib.cor.worker

fun ICorChainDsl<TaskContext>.taskStubCreateSuccess(title: String) = worker {
    this.title = title
    on { stubCase == TaskDebugStub.SUCCESS && state == AppState.RUNNING }
    handle {
        state = AppState.FINISHING
        val stub = TaskStub.prepareResult {
            taskRequest.type.takeIf { it != TaskType.NONE }?.also { this.type = it }
            taskRequest.priority.takeIf { it != TaskPriority.NONE }?.also { this.priority = it }
            taskRequest.status.takeIf { it != TaskStatus.NONE }?.also { this.status = it }
            taskRequest.title.takeIf { it.isNotBlank() }?.also { this.title = it }
            taskRequest.description.takeIf { it.isNotBlank() }?.also { this.description = it }
            taskRequest.executor.takeIf { it != UserId.NONE }?.also { this.executor = it }
            taskRequest.createdBy.takeIf { it != UserId.NONE }?.also { this.createdBy = it }
            taskRequest.permissions.takeIf { it.isEmpty() }?.also { this.permissions = it }
        }
        taskResponse = stub
    }
}

fun ICorChainDsl<TaskContext>.taskStubReadSuccess(title: String) = worker {
    this.title = title
    on { stubCase == TaskDebugStub.SUCCESS && state == AppState.RUNNING }
    handle {
        state = AppState.FINISHING
        val stub = TaskStub.prepareResult {
            taskRequest.title.takeIf { it.isNotBlank() }?.also { this.title = it }
        }
        taskResponse = stub
    }
}

fun ICorChainDsl<TaskContext>.taskStubUpdateSuccess(title: String) = worker {
    this.title = title
    on { stubCase == TaskDebugStub.SUCCESS && state == AppState.RUNNING }
    handle {
        state = AppState.FINISHING
        val stub = TaskStub.prepareResult {
            taskRequest.id.takeIf { it != TaskId.NONE }?.also { this.id = it }
            taskRequest.type.takeIf { it != TaskType.NONE }?.also { this.type = it }
            taskRequest.priority.takeIf { it != TaskPriority.NONE }?.also { this.priority = it }
            taskRequest.status.takeIf { it != TaskStatus.NONE }?.also { this.status = it }
            taskRequest.title.takeIf { it.isNotBlank() }?.also { this.title = it }
            taskRequest.description.takeIf { it.isNotBlank() }?.also { this.description = it }
            taskRequest.permissions.takeIf { it.isNotEmpty() }?.also { this.permissions = it }
        }
        taskResponse = stub
    }
}

fun ICorChainDsl<TaskContext>.taskStubDeleteSuccess(title: String) = worker {
    this.title = title
    on { stubCase == TaskDebugStub.SUCCESS && state == AppState.RUNNING }
    handle {
        state = AppState.FINISHING
        val stub = TaskStub.prepareResult {
            taskRequest.title.takeIf { it.isNotBlank() }?.also { this.title = it }
        }
        taskResponse = stub
    }
}

fun ICorChainDsl<TaskContext>.taskStubSearchSuccess(title: String) = worker {
    this.title = title
    on { stubCase == TaskDebugStub.SUCCESS && state == AppState.RUNNING }
    handle {
        state = AppState.FINISHING
        taskResponse = TaskStub.prepareResult {
            taskRequest.id.takeIf { it != TaskId.NONE }?.also { this.id = it }
        }
        tasksResponse.addAll(
            TaskStub.prepareSearchList(
                searchText = taskSearchFilterRequest.searchText,
                createdBy = taskSearchFilterRequest.createdBy,
                type = taskSearchFilterRequest.type,
                executor = taskSearchFilterRequest.executor,
            ),
        )
    }
}
