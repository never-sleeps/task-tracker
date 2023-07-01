package me.neversleeps.v2 // ktlint-disable filename

import me.neversleeps.api.multiplatform.v1.models.TaskCreateRequest
import me.neversleeps.api.multiplatform.v1.models.TaskDeleteRequest
import me.neversleeps.api.multiplatform.v1.models.TaskReadRequest
import me.neversleeps.api.multiplatform.v1.models.TaskSearchRequest
import me.neversleeps.api.multiplatform.v1.models.TaskUpdateRequest
import me.neversleeps.mappers.multiplatform.fromInternal.toTransportCreate
import me.neversleeps.mappers.multiplatform.fromInternal.toTransportDelete
import me.neversleeps.mappers.multiplatform.fromInternal.toTransportRead
import me.neversleeps.mappers.multiplatform.fromInternal.toTransportSearch
import me.neversleeps.mappers.multiplatform.fromInternal.toTransportUpdate
import me.neversleeps.mappers.multiplatform.fromTransport.fromTransport
import me.neversleeps.model.Request
import me.neversleeps.model.Response
import me.neversleeps.utils.toResponse
import me.neversleeps.utils.toTransportModel
import me.neversleeps.utils.withTaskContext
import yandex.cloud.sdk.functions.Context

object CreateTaskHandler : IV2HandleStrategy {
    override val path: String = "task/create"
    override fun handle(req: Request, reqContext: Context): Response {
        println("CreateTaskHandler v1 start")
        val request = req.toTransportModel<TaskCreateRequest>()
        return withTaskContext(reqContext) {
            fromTransport(request)
            taskResponse = TaskStub.get()
            toTransportCreate().toResponse()
        }
    }
}

object ReadTaskHandler : IV2HandleStrategy {
    override val path: String = "task/read"
    override fun handle(req: Request, reqContext: Context): Response {
        println("ReadTaskHandler v1 start")
        val request = req.toTransportModel<TaskReadRequest>()
        return withTaskContext(reqContext) {
            fromTransport(request)
            taskResponse = TaskStub.get()
            toTransportRead().toResponse()
        }
    }
}

object UpdateTaskHandler : IV2HandleStrategy {
    override val path: String = "task/update"
    override fun handle(req: Request, reqContext: Context): Response {
        println("UpdateTaskHandler v1 start")
        val request = req.toTransportModel<TaskUpdateRequest>()
        return withTaskContext(reqContext) {
            fromTransport(request)
            taskResponse = TaskStub.get()
            toTransportUpdate().toResponse()
        }
    }
}

object DeleteTaskHandler : IV2HandleStrategy {
    override val path: String = "task/delete"
    override fun handle(req: Request, reqContext: Context): Response {
        println("DeleteTaskHandler v1 start")
        val request = req.toTransportModel<TaskDeleteRequest>()
        return withTaskContext(reqContext) {
            fromTransport(request)
            toTransportDelete().toResponse()
        }
    }
}

object SearchTaskHandler : IV2HandleStrategy {
    override val path: String = "task/search"
    override fun handle(req: Request, reqContext: Context): Response {
        println("SearchTaskHandler v1 start")
        val request = req.toTransportModel<TaskSearchRequest>()
        return withTaskContext(reqContext) {
            fromTransport(request)
            tasksResponse = TaskStub.getList()
            toTransportSearch().toResponse()
        }
    }
}
