package ru.otus.otuskotlin.marketplace.common

import kotlinx.datetime.Instant
import ru.otus.otuskotlin.marketplace.common.models.AppCommand
import ru.otus.otuskotlin.marketplace.common.models.AppError
import ru.otus.otuskotlin.marketplace.common.models.AppState
import ru.otus.otuskotlin.marketplace.common.models.RequestId
import ru.otus.otuskotlin.marketplace.common.models.project.Project
import ru.otus.otuskotlin.marketplace.common.models.project.ProjectFilter
import ru.otus.otuskotlin.marketplace.common.stubs.ProjectStub

data class ProjectContext(
    override var requestId: RequestId = RequestId.NONE,
    override var timeStart: Instant = Instant.NONE,
    override var command: AppCommand = AppCommand.NONE,
    override var state: AppState = AppState.NONE,
    override var errors: MutableList<AppError> = mutableListOf(),

    var projectRequest: Project = Project(),
    var projectFilterRequest: ProjectFilter = ProjectFilter(),
    var projectResponse: Project = Project(),
    var projectsResponse: MutableList<Project> = mutableListOf(),
    var stubCase: ProjectStub = ProjectStub.NONE,
) : IContext
