package me.neversleeps.common

import kotlinx.datetime.Instant
import me.neversleeps.common.models.AppCommand
import me.neversleeps.common.models.AppError
import me.neversleeps.common.models.AppState
import me.neversleeps.common.models.RequestId
import me.neversleeps.common.models.project.Project
import me.neversleeps.common.models.project.ProjectSearchFilter
import me.neversleeps.common.stubs.ProjectDebugStub

data class ProjectContext(
    override var requestId: RequestId = RequestId.NONE,
    override var timeStart: Instant = Instant.NONE,
    override var command: AppCommand = AppCommand.NONE,
    override var state: AppState = AppState.NONE,
    override var errors: MutableList<AppError> = mutableListOf(),

    var projectRequest: Project = Project(),
    var projectSearchFilterRequest: ProjectSearchFilter = ProjectSearchFilter(),
    var projectResponse: Project = Project(),
    var projectsResponse: MutableList<Project> = mutableListOf(),
    var stubCase: ProjectDebugStub = ProjectDebugStub.NONE,
) : IContext
