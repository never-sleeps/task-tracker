package fromTransport

import ProjectContext
import UnknownRequestClass
import me.neversleeps.api.multiplatform.v1.models.IRequest
import me.neversleeps.api.multiplatform.v1.models.ProjectCreateRequest
import me.neversleeps.api.multiplatform.v1.models.ProjectDeleteRequest
import me.neversleeps.api.multiplatform.v1.models.ProjectReadRequest
import me.neversleeps.api.multiplatform.v1.models.ProjectSearchRequest
import me.neversleeps.api.multiplatform.v1.models.ProjectUpdateRequest
import models.AppCommand
import models.project.Project
import models.project.ProjectSearchFilter

fun ProjectContext.fromTransport(request: IRequest) = when (request) {
    is ProjectCreateRequest -> fromTransport(request)
    is ProjectReadRequest -> fromTransport(request)
    is ProjectUpdateRequest -> fromTransport(request)
    is ProjectDeleteRequest -> fromTransport(request)
    is ProjectSearchRequest -> fromTransport(request)
    else -> throw UnknownRequestClass(request::class)
}

fun ProjectContext.fromTransport(request: ProjectCreateRequest) {
    requestId = request.toRequestId()
    command = AppCommand.CREATE
    projectRequest = request.data?.toInternal() ?: Project()
    stubCase = request.stub?.let { it.toInternal() } ?: stubs.ProjectDebugStub.NONE
}

fun ProjectContext.fromTransport(request: ProjectReadRequest) {
    requestId = request.toRequestId()
    command = AppCommand.READ
    projectRequest = request.id.toProjectWithId()
    stubCase = request.stub?.let { it.toInternal() } ?: stubs.ProjectDebugStub.NONE
}

fun ProjectContext.fromTransport(request: ProjectUpdateRequest) {
    requestId = request.toRequestId()
    command = AppCommand.UPDATE
    projectRequest = request.data?.toInternal() ?: Project()
    stubCase = request.stub?.let { it.toInternal() } ?: stubs.ProjectDebugStub.NONE
}

fun ProjectContext.fromTransport(request: ProjectDeleteRequest) {
    requestId = request.toRequestId()
    command = AppCommand.DELETE
    projectRequest = request.id.toProjectWithId()
    stubCase = request.stub?.let { it.toInternal() } ?: stubs.ProjectDebugStub.NONE
}

fun ProjectContext.fromTransport(request: ProjectSearchRequest) {
    requestId = request.toRequestId()
    command = AppCommand.SEARCH
    projectSearchFilterRequest = request.filter?.toInternal() ?: ProjectSearchFilter()
    stubCase = request.stub?.let { it.toInternal() } ?: stubs.ProjectDebugStub.NONE
}
