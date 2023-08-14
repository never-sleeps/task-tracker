package me.neversleeps.common

import kotlinx.datetime.Instant
import me.neversleeps.common.models.AppCommand
import me.neversleeps.common.models.AppError
import me.neversleeps.common.models.AppState
import me.neversleeps.common.models.RequestId
import me.neversleeps.common.models.project.Project
import me.neversleeps.common.models.project.ProjectSearchFilter
import me.neversleeps.common.repository.project.IProjectRepository
import me.neversleeps.common.stubs.WorkMode
import me.neversleeps.common.stubs.ProjectDebugStub

data class ProjectContext(
    override var requestId: RequestId = RequestId.NONE,
    override var timeStart: Instant = Instant.NONE,
    override var command: AppCommand = AppCommand.NONE,
    override var state: AppState = AppState.NONE,
    override var errors: MutableList<AppError> = mutableListOf(),
    var settings: CorSettings = CorSettings.NONE,

    var stubCase: ProjectDebugStub = ProjectDebugStub.NONE,
    var workMode: WorkMode = WorkMode.PROD,

    // project-validation (there is not for task)
    var projectValidating: Project = Project(),
    var projectSearchFilterValidating: ProjectSearchFilter = ProjectSearchFilter(),
    var projectValidated: Project = Project(),
    var projectSearchFilterValidated: ProjectSearchFilter = ProjectSearchFilter(),

    // requests
    var projectRequest: Project = Project(),
    var projectSearchFilterRequest: ProjectSearchFilter = ProjectSearchFilter(),
    // responses
    var projectResponse: Project = Project(),
    var projectsResponse: MutableList<Project> = mutableListOf(),

    var projectRepository: IProjectRepository = IProjectRepository.NONE,
    var projectRepositoryPrepare: Project = Project(),
    var projectRepositoryRead: Project = Project(),
    var projectRepositoryDone: Project = Project(),
    var projectsRepositoryDone: MutableList<Project> = mutableListOf(),
) : IContext
