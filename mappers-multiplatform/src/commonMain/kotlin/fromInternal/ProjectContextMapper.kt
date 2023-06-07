package fromInternal

import ProjectContext
import UnknownCommandMapping
import me.neversleeps.api.multiplatform.v1.models.IResponse
import me.neversleeps.api.multiplatform.v1.models.ProjectCreateResponse
import me.neversleeps.api.multiplatform.v1.models.ProjectDeleteResponse
import me.neversleeps.api.multiplatform.v1.models.ProjectReadResponse
import me.neversleeps.api.multiplatform.v1.models.ProjectSearchResponse
import me.neversleeps.api.multiplatform.v1.models.ProjectUpdateResponse
import models.AppCommand

fun ProjectContext.toTransport(): IResponse = when (val cmd = command) {
    AppCommand.CREATE -> toTransportCreate()
    AppCommand.READ -> toTransportRead()
    AppCommand.UPDATE -> toTransportUpdate()
    AppCommand.DELETE -> toTransportDelete()
    AppCommand.SEARCH -> toTransportSearch()
    AppCommand.NONE -> throw UnknownCommandMapping(cmd)
}

fun ProjectContext.toTransportCreate() = ProjectCreateResponse(
    requestId = this.requestId.asString(),
    resultStatus = state.toTransport(),
    errors = errors.toTransport(),
    project = projectResponse.toTransport(),
)

fun ProjectContext.toTransportRead() = ProjectReadResponse(
    requestId = this.requestId.asString(),
    resultStatus = state.toTransport(),
    errors = errors.toTransport(),
    project = projectResponse.toTransport(),
)

fun ProjectContext.toTransportUpdate() = ProjectUpdateResponse(
    requestId = this.requestId.asString(),
    resultStatus = state.toTransport(),
    errors = errors.toTransport(),
    project = projectResponse.toTransport(),
)

fun ProjectContext.toTransportDelete() = ProjectDeleteResponse(
    requestId = this.requestId.asString(),
    resultStatus = state.toTransport(),
    errors = errors.toTransport(),
    project = projectResponse.toTransport(),
)

fun ProjectContext.toTransportSearch() = ProjectSearchResponse(
    requestId = this.requestId.asString(),
    resultStatus = state.toTransport(),
    errors = errors.toTransport(),
    projects = projectsResponse.toTransport(),
)
