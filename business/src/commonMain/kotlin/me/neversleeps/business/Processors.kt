package me.neversleeps.business

import ProjectStub
import exception.UnknownCommand
import me.neversleeps.common.ProjectContext
import me.neversleeps.common.TaskContext
import me.neversleeps.common.models.AppCommand

class ProjectProcessor {
    suspend fun execute(ctx: ProjectContext) {
        when (ctx.command) {
            AppCommand.CREATE -> ctx.projectResponse = ProjectStub.get()
            AppCommand.READ -> ctx.projectResponse = ProjectStub.get()
            AppCommand.UPDATE -> ctx.projectResponse = ProjectStub.get()
            AppCommand.DELETE -> ctx.projectResponse = ProjectStub.get()
            AppCommand.SEARCH -> ctx.projectsResponse = ProjectStub.getList()
            else -> throw UnknownCommand(ctx.command)
        }
    }
}

class TaskProcessor {
    suspend fun execute(ctx: TaskContext) {
        when (ctx.command) {
            AppCommand.CREATE -> ctx.taskResponse = TaskStub.get()
            AppCommand.READ -> ctx.taskResponse = TaskStub.get()
            AppCommand.UPDATE -> ctx.taskResponse = TaskStub.get()
            AppCommand.DELETE -> ctx.taskResponse = TaskStub.get()
            AppCommand.SEARCH -> ctx.tasksResponse = TaskStub.getList()
            else -> throw UnknownCommand(ctx.command)
        }
    }
}
