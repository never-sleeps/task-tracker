package me.neversleeps.v1 // ktlint-disable filename

import me.neversleeps.api.jackson.v1.models.ProjectCreateRequest
import me.neversleeps.api.jackson.v1.models.ProjectDeleteRequest
import me.neversleeps.api.jackson.v1.models.ProjectReadRequest
import me.neversleeps.api.jackson.v1.models.ProjectSearchRequest
import me.neversleeps.api.jackson.v1.models.ProjectUpdateRequest
import me.neversleeps.mappers.jackson.fromInternal.toTransportCreate
import me.neversleeps.mappers.jackson.fromInternal.toTransportDelete
import me.neversleeps.mappers.jackson.fromInternal.toTransportRead
import me.neversleeps.mappers.jackson.fromInternal.toTransportSearch
import me.neversleeps.mappers.jackson.fromInternal.toTransportUpdate
import me.neversleeps.mappers.jackson.fromTransport.fromTransport
import me.neversleeps.model.Request
import me.neversleeps.model.Response
import me.neversleeps.utils.toResponse
import me.neversleeps.utils.toTransportModel
import me.neversleeps.utils.withProjectContext
import yandex.cloud.sdk.functions.Context

object CreateProjectHandler : IV1HandleStrategy {
    override val path: String = "project/create"
    override fun handle(req: Request, reqContext: Context): Response {
        println("CreateProjectHandler v1 start")
        val request = req.toTransportModel<ProjectCreateRequest>()
        return withProjectContext(reqContext) {
            fromTransport(request)
            projectResponse = ProjectStub.get()
            toTransportCreate().toResponse()
        }
    }
}

object ReadProjectHandler : IV1HandleStrategy {
    override val path: String = "project/read"
    override fun handle(req: Request, reqContext: Context): Response {
        println("ReadProjectHandler v1 start")
        val request = req.toTransportModel<ProjectReadRequest>()
        return withProjectContext(reqContext) {
            fromTransport(request)
            projectResponse = ProjectStub.get()
            toTransportRead().toResponse()
        }
    }
}

object UpdateProjectHandler : IV1HandleStrategy {
    override val path: String = "project/update"
    override fun handle(req: Request, reqContext: Context): Response {
        println("UpdateProjectHandler v1 start")
        val request = req.toTransportModel<ProjectUpdateRequest>()
        return withProjectContext(reqContext) {
            fromTransport(request)
            projectResponse = ProjectStub.get()
            toTransportUpdate().toResponse()
        }
    }
}

object DeleteProjectHandler : IV1HandleStrategy {
    override val path: String = "project/delete"
    override fun handle(req: Request, reqContext: Context): Response {
        println("DeleteProjectHandler v1 start")
        val request = req.toTransportModel<ProjectDeleteRequest>()
        return withProjectContext(reqContext) {
            fromTransport(request)
            toTransportDelete().toResponse()
        }
    }
}

object SearchProjectHandler : IV1HandleStrategy {
    override val path: String = "project/search"
    override fun handle(req: Request, reqContext: Context): Response {
        println("SearchProjectHandler v1 start")
        val request = req.toTransportModel<ProjectSearchRequest>()
        return withProjectContext(reqContext) {
            fromTransport(request)
            projectsResponse = ProjectStub.getList()
            toTransportSearch().toResponse()
        }
    }
}
