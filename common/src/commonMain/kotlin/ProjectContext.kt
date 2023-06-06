import kotlinx.datetime.Instant
import models.AppCommand
import models.AppError
import models.AppState
import models.RequestId
import models.project.Project
import models.project.ProjectSearchFilter
import stubs.ProjectDebugStub

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
