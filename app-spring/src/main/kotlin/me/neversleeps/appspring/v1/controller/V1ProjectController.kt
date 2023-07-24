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
import me.neversleeps.business.ProjectProcessor
import me.neversleeps.common.CorSettings
import me.neversleeps.common.models.AppCommand
import org.springframework.web.bind.annotation.* // ktlint-disable no-wildcard-imports

@RestController
@RequestMapping("api/v1/project")
class V1ProjectController(
    private val processor: ProjectProcessor,
    settings: CorSettings,
) {

    private val logger = settings.loggerProvider.logger(V1ProjectController::class)

    @PostMapping("create")
    suspend fun createProject(@RequestBody request: ProjectCreateRequest): ProjectCreateResponse {
        return processV1(
            processor = processor,
            command = AppCommand.CREATE,
            request = request,
            logger = logger,
            logId = "project-create",
        )
    }

    @PostMapping("read")
    suspend fun readProject(@RequestBody request: ProjectReadRequest): ProjectReadResponse {
        return processV1(
            processor = processor,
            command = AppCommand.READ,
            request = request,
            logger = logger,
            logId = "project-read",
        )
    }

    @PostMapping("update")
    suspend fun updateProject(@RequestBody request: ProjectUpdateRequest): ProjectUpdateResponse {
        return processV1(
            processor = processor,
            command = AppCommand.UPDATE,
            request = request,
            logger = logger,
            logId = "project-update",
        )
    }

    @PostMapping("delete")
    suspend fun deleteProject(@RequestBody request: ProjectDeleteRequest): ProjectDeleteResponse {
        return processV1(
            processor = processor,
            command = AppCommand.DELETE,
            request = request,
            logger = logger,
            logId = "project-delete",
        )
    }

    @PostMapping("search")
    suspend fun searchProject(@RequestBody request: ProjectSearchRequest): ProjectSearchResponse {
        return processV1(
            processor = processor,
            command = AppCommand.SEARCH,
            request = request,
            logger = logger,
            logId = "project-search",
        )
    }
}
