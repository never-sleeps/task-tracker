package me.neversleeps.appspring.api.v1.controller.web

import me.neversleeps.api.jackson.v1.models.TaskCreateRequest
import me.neversleeps.api.jackson.v1.models.TaskDeleteRequest
import me.neversleeps.api.jackson.v1.models.TaskReadRequest
import me.neversleeps.api.jackson.v1.models.TaskSearchRequest
import me.neversleeps.api.jackson.v1.models.TaskUpdateRequest
import me.neversleeps.appspring.service.TaskBlockingProcessor
import me.neversleeps.appspring.v1.controller.V1TaskController
import me.neversleeps.common.TaskContext
import me.neversleeps.mappers.jackson.fromInternal.toTransportCreate
import me.neversleeps.mappers.jackson.fromInternal.toTransportDelete
import me.neversleeps.mappers.jackson.fromInternal.toTransportRead
import me.neversleeps.mappers.jackson.fromInternal.toTransportSearch
import me.neversleeps.mappers.jackson.fromInternal.toTransportUpdate
import org.junit.jupiter.api.Test
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean

@WebMvcTest(V1TaskController::class)
internal class TaskControllerTest : BaseControllerTest() {

    @MockBean
    private lateinit var processor: TaskBlockingProcessor

    @Test
    fun create() {
        testStub(
            "/api/v1/task/create",
            TaskCreateRequest(),
            TaskContext().toTransportCreate(),
        )
    }

    @Test
    fun read() {
        testStub(
            "/api/v1/task/read",
            TaskReadRequest(),
            TaskContext().toTransportRead(),
        )
    }

    @Test
    fun update() {
        testStub(
            "/api/v1/task/update",
            TaskUpdateRequest(),
            TaskContext().toTransportUpdate(),
        )
    }

    @Test
    fun delete() {
        testStub(
            "/api/v1/task/delete",
            TaskDeleteRequest(),
            TaskContext().toTransportDelete(),
        )
    }

    @Test
    fun search() {
        testStub(
            "/api/v1/task/search",
            TaskSearchRequest(),
            TaskContext().toTransportSearch(),
        )
    }
}
