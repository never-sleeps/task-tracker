package me.neversleeps.mappers.multiplatform.fromInternal

import me.neversleeps.api.multiplatform.v1.models.IResponse
import me.neversleeps.api.multiplatform.v1.models.InitBaseResponse
import me.neversleeps.api.multiplatform.v1.models.ProjectCreateResponse
import me.neversleeps.api.multiplatform.v1.models.ProjectDeleteResponse
import me.neversleeps.api.multiplatform.v1.models.ProjectReadResponse
import me.neversleeps.api.multiplatform.v1.models.ProjectSearchResponse
import me.neversleeps.api.multiplatform.v1.models.ProjectUpdateResponse
import me.neversleeps.api.multiplatform.v1.models.ResponseResultStatus
import me.neversleeps.common.ProjectContext
import me.neversleeps.common.models.AppCommand
import me.neversleeps.mappers.multiplatform.UnknownCommandMapping

fun ProjectContext.toTransport(): IResponse = when (val cmd = command) {
    AppCommand.CREATE -> toTransportCreate()
    AppCommand.READ -> toTransportRead()
    AppCommand.UPDATE -> toTransportUpdate()
    AppCommand.DELETE -> toTransportDelete()
    AppCommand.SEARCH -> toTransportSearch()
    AppCommand.NONE -> throw UnknownCommandMapping(cmd)
}

fun ProjectContext.toTransportInit() = InitBaseResponse(
    requestId = this.requestId.asString(),
    resultStatus = if (errors.isEmpty()) ResponseResultStatus.SUCCESS else ResponseResultStatus.ERROR,
    errors = errors.toTransport(),
)

fun ProjectContext.toTransportCreate() = ProjectCreateResponse(
    responseType = "createProject",
    requestId = this.requestId.asString(),
    resultStatus = state.toTransport(),
    errors = errors.toTransport(),
    project = projectResponse.toTransport(),
)

fun ProjectContext.toTransportRead() = ProjectReadResponse(
    responseType = "readProject",
    requestId = this.requestId.asString(),
    resultStatus = state.toTransport(),
    errors = errors.toTransport(),
    project = projectResponse.toTransport(),
)

fun ProjectContext.toTransportUpdate() = ProjectUpdateResponse(
    responseType = "updateProject",
    requestId = this.requestId.asString(),
    resultStatus = state.toTransport(),
    errors = errors.toTransport(),
    project = projectResponse.toTransport(),
)

fun ProjectContext.toTransportDelete() = ProjectDeleteResponse(
    responseType = "deleteProject",
    requestId = this.requestId.asString(),
    resultStatus = state.toTransport(),
    errors = errors.toTransport(),
    project = projectResponse.toTransport(),
)

fun ProjectContext.toTransportSearch() = ProjectSearchResponse(
    responseType = "searchProject",
    requestId = this.requestId.asString(),
    resultStatus = state.toTransport(),
    errors = errors.toTransport(),
    projects = projectsResponse.toTransport(),
)
