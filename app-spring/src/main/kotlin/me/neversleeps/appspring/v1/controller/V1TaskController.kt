package me.neversleeps.appspring.v1.controller

import me.neversleeps.api.jackson.v1.models.TaskCreateRequest
import me.neversleeps.api.jackson.v1.models.TaskCreateResponse
import me.neversleeps.api.jackson.v1.models.TaskDeleteRequest
import me.neversleeps.api.jackson.v1.models.TaskDeleteResponse
import me.neversleeps.api.jackson.v1.models.TaskReadRequest
import me.neversleeps.api.jackson.v1.models.TaskReadResponse
import me.neversleeps.api.jackson.v1.models.TaskSearchRequest
import me.neversleeps.api.jackson.v1.models.TaskSearchResponse
import me.neversleeps.api.jackson.v1.models.TaskUpdateRequest
import me.neversleeps.api.jackson.v1.models.TaskUpdateResponse
import me.neversleeps.appspring.service.TaskBlockingProcessor
import me.neversleeps.common.TaskContext
import me.neversleeps.mappers.jackson.fromInternal.toTransportCreate
import me.neversleeps.mappers.jackson.fromInternal.toTransportDelete
import me.neversleeps.mappers.jackson.fromInternal.toTransportRead
import me.neversleeps.mappers.jackson.fromInternal.toTransportSearch
import me.neversleeps.mappers.jackson.fromInternal.toTransportUpdate
import me.neversleeps.mappers.jackson.fromTransport.fromTransport
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("api/v1/task")
class V1TaskController(
    private val processor: TaskBlockingProcessor,
) {

    @PostMapping("create")
    fun create(@RequestBody request: TaskCreateRequest): TaskCreateResponse {
        val context = TaskContext().apply { this.fromTransport(request) }
        processor.execute(context)
        return context.toTransportCreate()
    }

    @PostMapping("read")
    fun read(@RequestBody request: TaskReadRequest): TaskReadResponse {
        val context = TaskContext().apply { this.fromTransport(request) }
        processor.execute(context)
        return context.toTransportRead()
    }

    @PostMapping("update")
    fun update(@RequestBody request: TaskUpdateRequest): TaskUpdateResponse {
        val context = TaskContext().apply { this.fromTransport(request) }
        processor.execute(context)
        return context.toTransportUpdate()
    }

    @PostMapping("delete")
    fun delete(@RequestBody request: TaskDeleteRequest): TaskDeleteResponse {
        val context = TaskContext().apply { this.fromTransport(request) }
        processor.execute(context)
        return context.toTransportDelete()
    }

    @PostMapping("search")
    fun search(@RequestBody request: TaskSearchRequest): TaskSearchResponse {
        val context = TaskContext().apply { this.fromTransport(request) }
        processor.execute(context)
        return context.toTransportSearch()
    }
}
