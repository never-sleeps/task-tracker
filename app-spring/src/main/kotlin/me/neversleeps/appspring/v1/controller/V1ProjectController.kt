package me.neversleeps.appspring.v1.controller

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
import me.neversleeps.appspring.service.ProjectBlockingProcessor
import me.neversleeps.common.ProjectContext
import me.neversleeps.mappers.jackson.fromInternal.toTransportCreate
import me.neversleeps.mappers.jackson.fromInternal.toTransportDelete
import me.neversleeps.mappers.jackson.fromInternal.toTransportRead
import me.neversleeps.mappers.jackson.fromInternal.toTransportSearch
import me.neversleeps.mappers.jackson.fromInternal.toTransportUpdate
import me.neversleeps.mappers.jackson.fromTransport.fromTransport
import org.springframework.web.bind.annotation.* // ktlint-disable no-wildcard-imports

@RestController
@RequestMapping("api/v1/project")
class V1ProjectController(
    private val processor: ProjectBlockingProcessor,
) {

    @PostMapping("create")
    fun createProject(@RequestBody request: ProjectCreateRequest): ProjectCreateResponse {
        val context = ProjectContext().apply { this.fromTransport(request) }
        processor.execute(context)
        return context.toTransportCreate()
    }

    @PostMapping("read")
    fun readProject(@RequestBody request: ProjectReadRequest): ProjectReadResponse {
        val context = ProjectContext().apply { this.fromTransport(request) }
        processor.execute(context)
        return context.toTransportRead()
    }

    @PostMapping("update")
    fun updateProject(@RequestBody request: ProjectUpdateRequest): ProjectUpdateResponse {
        val context = ProjectContext().apply { this.fromTransport(request) }
        processor.execute(context)
        return context.toTransportUpdate()
    }

    @PostMapping("delete")
    fun deleteProject(@RequestBody request: ProjectDeleteRequest): ProjectDeleteResponse {
        val context = ProjectContext().apply { this.fromTransport(request) }
        processor.execute(context)
        return context.toTransportDelete()
    }

    @PostMapping("search")
    fun searchProject(@RequestBody request: ProjectSearchRequest): ProjectSearchResponse {
        val context = ProjectContext().apply { this.fromTransport(request) }
        processor.execute(context)
        return context.toTransportSearch()
    }
}
