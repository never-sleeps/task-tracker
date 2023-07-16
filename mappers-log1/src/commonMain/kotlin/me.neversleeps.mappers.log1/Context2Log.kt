package me.neversleeps.mappers.log1

import kotlinx.datetime.Clock
import me.neversleeps.api.logs.models.* // ktlint-disable no-wildcard-imports
import me.neversleeps.common.ProjectContext
import me.neversleeps.common.models.AppError
import me.neversleeps.common.models.RequestId
import me.neversleeps.common.models.project.Project
import me.neversleeps.common.models.project.ProjectId
import me.neversleeps.common.models.project.ProjectSearchFilter
import me.neversleeps.common.models.user.UserId

fun ProjectContext.toLog(logId: String) = CommonLogModel(
    messageTime = Clock.System.now().toString(),
    logId = logId,
    source = "task-tracker",
    project = toLog(),
    errors = errors.map { it.toLog() },
)

fun ProjectContext.toLog(): LogModel? {
    val projectNone = Project()
    return LogModel(
        requestId = requestId.takeIf { it != RequestId.NONE }?.asString(),
        requestProject = projectRequest.takeIf { it != projectNone }?.toLog(),
        responseProject = projectResponse.takeIf { it != projectNone }?.toLog(),
        responseProjects = projectsResponse.takeIf { it.isNotEmpty() }
            ?.filter { it != projectNone }
            ?.map { it.toLog() },
        requestFilter = projectSearchFilterRequest.takeIf { it != ProjectSearchFilter() }?.toLog(),
    ).takeIf { it != LogModel() }
}

private fun ProjectSearchFilter.toLog() = ProjectFilterLog(
    searchText = searchText.takeIf { it.isNotBlank() },
    createdBy = createdBy.takeIf { it != UserId.NONE }?.asString(),
)

fun AppError.toLog() = ErrorLogModel(
    message = message.takeIf { it.isNotBlank() },
    field = field.takeIf { it.isNotBlank() },
    code = code.takeIf { it.isNotBlank() },
    level = level.name,
)

fun Project.toLog() = ProjectLog(
    id = id.takeIf { it != ProjectId.NONE }?.asString(),
    title = title.takeIf { it.isNotBlank() },
    description = description.takeIf { it.isNotBlank() },
)
