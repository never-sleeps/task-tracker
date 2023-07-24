package me.neversleeps.app.multiplatform

import io.ktor.server.application.* // ktlint-disable no-wildcard-imports
import me.neversleeps.api.multiplatform.v1.models.ProjectCreateRequest
import me.neversleeps.api.multiplatform.v1.models.ProjectCreateResponse
import me.neversleeps.api.multiplatform.v1.models.ProjectDeleteRequest
import me.neversleeps.api.multiplatform.v1.models.ProjectDeleteResponse
import me.neversleeps.api.multiplatform.v1.models.ProjectReadRequest
import me.neversleeps.api.multiplatform.v1.models.ProjectReadResponse
import me.neversleeps.api.multiplatform.v1.models.ProjectSearchRequest
import me.neversleeps.api.multiplatform.v1.models.ProjectSearchResponse
import me.neversleeps.api.multiplatform.v1.models.ProjectUpdateRequest
import me.neversleeps.api.multiplatform.v1.models.ProjectUpdateResponse
import me.neversleeps.app.AppSettings
import me.neversleeps.common.models.AppCommand
import me.neversleeps.logging.common.ILogWrapper

suspend fun ApplicationCall.createProject(appSettings: AppSettings, logger: ILogWrapper) {
    processV2<ProjectCreateRequest, ProjectCreateResponse>(
        appSettings = appSettings,
        logger = logger,
        logId = "project-create",
        command = AppCommand.CREATE,
    )
}

suspend fun ApplicationCall.readProject(appSettings: AppSettings, logger: ILogWrapper) {
    processV2<ProjectReadRequest, ProjectReadResponse>(
        appSettings = appSettings,
        logger = logger,
        logId = "project-read",
        command = AppCommand.READ,
    )
}

suspend fun ApplicationCall.updateProject(appSettings: AppSettings, logger: ILogWrapper) {
    processV2<ProjectUpdateRequest, ProjectUpdateResponse>(
        appSettings = appSettings,
        logger = logger,
        logId = "project-update",
        command = AppCommand.READ,
    )
}

suspend fun ApplicationCall.deleteProject(appSettings: AppSettings, logger: ILogWrapper) {
    processV2<ProjectDeleteRequest, ProjectDeleteResponse>(
        appSettings = appSettings,
        logger = logger,
        logId = "project-delete",
        command = AppCommand.READ,
    )
}

suspend fun ApplicationCall.searchProject(appSettings: AppSettings, logger: ILogWrapper) {
    processV2<ProjectSearchRequest, ProjectSearchResponse>(
        appSettings = appSettings,
        logger = logger,
        logId = "project-search",
        command = AppCommand.READ,
    )
}
