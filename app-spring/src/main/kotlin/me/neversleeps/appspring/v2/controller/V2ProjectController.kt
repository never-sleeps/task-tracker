package me.neversleeps.appspring.v2.controller

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
import me.neversleeps.business.ProjectProcessor
import me.neversleeps.common.CorSettings
import me.neversleeps.common.models.AppCommand
import org.springframework.web.bind.annotation.* // ktlint-disable no-wildcard-imports

@RestController
@RequestMapping("api/v2/project")
class V2ProjectController(
    private val processor: ProjectProcessor,
    settings: CorSettings,
) {
    private val logger = settings.loggerProvider.logger(V2ProjectController::class)

    @PostMapping("create")
    suspend fun createProject(@RequestBody request: String): String {
        return processV2<ProjectCreateRequest, ProjectCreateResponse>(
            processor = processor,
            command = AppCommand.CREATE,
            requestString = request,
            logger = logger,
            logId = "project-create",
        )
    }

    @PostMapping("read")
    suspend fun readProject(@RequestBody request: String): String {
        return processV2<ProjectReadRequest, ProjectReadResponse>(
            processor = processor,
            command = AppCommand.READ,
            requestString = request,
            logger = logger,
            logId = "project-read",
        )
    }

    @PostMapping("update")
    suspend fun updateProject(@RequestBody request: String): String {
        return processV2<ProjectUpdateRequest, ProjectUpdateResponse>(
            processor = processor,
            command = AppCommand.UPDATE,
            requestString = request,
            logger = logger,
            logId = "project-update",
        )
    }

    @PostMapping("delete")
    suspend fun deleteProject(@RequestBody request: String): String {
        return processV2<ProjectDeleteRequest, ProjectDeleteResponse>(
            processor = processor,
            command = AppCommand.DELETE,
            requestString = request,
            logger = logger,
            logId = "project-delete",
        )
    }

    @PostMapping("search")
    suspend fun searchProject(@RequestBody request: String): String {
        return processV2<ProjectSearchRequest, ProjectSearchResponse>(
            processor = processor,
            command = AppCommand.SEARCH,
            requestString = request,
            logger = logger,
            logId = "project-search",
        )
    }
}
