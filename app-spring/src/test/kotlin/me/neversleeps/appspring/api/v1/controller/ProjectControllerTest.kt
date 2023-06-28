package me.neversleeps.appspring.api.v1.controller

import me.neversleeps.api.jackson.v1.models.ProjectCreateRequest
import me.neversleeps.api.jackson.v1.models.ProjectDeleteRequest
import me.neversleeps.api.jackson.v1.models.ProjectReadRequest
import me.neversleeps.api.jackson.v1.models.ProjectSearchRequest
import me.neversleeps.api.jackson.v1.models.ProjectUpdateRequest
import me.neversleeps.appspring.controller.v1.ProjectController
import me.neversleeps.appspring.service.ProjectBlockingProcessor
import me.neversleeps.common.ProjectContext
import me.neversleeps.mappers.jackson.fromInternal.toTransportCreate
import me.neversleeps.mappers.jackson.fromInternal.toTransportDelete
import me.neversleeps.mappers.jackson.fromInternal.toTransportRead
import me.neversleeps.mappers.jackson.fromInternal.toTransportSearch
import me.neversleeps.mappers.jackson.fromInternal.toTransportUpdate
import org.junit.jupiter.api.Test
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean

@WebMvcTest(ProjectController::class)
internal class ProjectControllerTest : BaseControllerTest() {

    @MockBean
    private lateinit var processor: ProjectBlockingProcessor

    @Test
    fun createProject() {
        testStub(
            "/api/v1/project/create",
            ProjectCreateRequest(),
            ProjectContext().toTransportCreate(),
        )
    }

    @Test
    fun readProject() {
        testStub(
            "/api/v1/project/read",
            ProjectReadRequest(),
            ProjectContext().toTransportRead(),
        )
    }

    @Test
    fun updateProject() {
        testStub(
            "/api/v1/project/update",
            ProjectUpdateRequest(),
            ProjectContext().toTransportUpdate(),
        )
    }

    @Test
    fun deleteProject() {
        testStub(
            "/api/v1/project/delete",
            ProjectDeleteRequest(),
            ProjectContext().toTransportDelete(),
        )
    }

    @Test
    fun searchProject() {
        testStub(
            "/api/v1/project/search",
            ProjectSearchRequest(),
            ProjectContext().toTransportSearch(),
        )
    }
}
