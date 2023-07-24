package me.neversleeps.app.jackson

import io.ktor.server.application.* // ktlint-disable no-wildcard-imports
import me.neversleeps.api.jackson.v1.models.ProjectCreateRequest
import me.neversleeps.api.jackson.v1.models.ProjectCreateResponse
import me.neversleeps.api.jackson.v1.models.ProjectDeleteRequest
import me.neversleeps.api.jackson.v1.models.ProjectDeleteResponse
import me.neversleeps.api.jackson.v1.models.ProjectReadRequest
import me.neversleeps.api.jackson.v1.models.ProjectReadResponse
import me.neversleeps.api.jackson.v1.models.ProjectSearchRequest
import me.neversleeps.api.jackson.v1.models.ProjectSearchResponse
import me.neversleeps.api.jackson.v1.models.ProjectUpdateRequest
import me.neversleeps.api.jackson.v1.models.ProjectUpdateResponse
import me.neversleeps.app.AppSettings
import me.neversleeps.common.models.AppCommand
import me.neversleeps.logging.common.ILogWrapper

suspend fun ApplicationCall.createProject(appSettings: AppSettings, logger: ILogWrapper) {
    processV1<ProjectCreateRequest, ProjectCreateResponse>(
        appSettings = appSettings,
        logger = logger,
        logId = "project-create",
        command = AppCommand.CREATE,
    )
}

suspend fun ApplicationCall.readProject(appSettings: AppSettings, logger: ILogWrapper) {
    processV1<ProjectReadRequest, ProjectReadResponse>(
        appSettings = appSettings,
        logger = logger,
        logId = "project-read",
        command = AppCommand.READ,
    )
}

suspend fun ApplicationCall.updateProject(appSettings: AppSettings, logger: ILogWrapper) {
    processV1<ProjectUpdateRequest, ProjectUpdateResponse>(
        appSettings = appSettings,
        logger = logger,
        logId = "project-update",
        command = AppCommand.UPDATE,
    )
}

suspend fun ApplicationCall.deleteProject(appSettings: AppSettings, logger: ILogWrapper) {
    processV1<ProjectDeleteRequest, ProjectDeleteResponse>(
        appSettings = appSettings,
        logger = logger,
        logId = "project-delete",
        command = AppCommand.DELETE,
    )
}

suspend fun ApplicationCall.searchProject(appSettings: AppSettings, logger: ILogWrapper) {
    processV1<ProjectSearchRequest, ProjectSearchResponse>(
        appSettings = appSettings,
        logger = logger,
        logId = "project-search",
        command = AppCommand.SEARCH,
    )
}
