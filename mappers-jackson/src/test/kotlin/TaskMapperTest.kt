package me.neversleeps.mappers.jackson

import me.neversleeps.api.jackson.v1.models.ResponseResultStatus
import me.neversleeps.api.jackson.v1.models.TaskCreateObject
import me.neversleeps.api.jackson.v1.models.TaskCreateRequest
import me.neversleeps.api.jackson.v1.models.TaskCreateResponse
import me.neversleeps.api.jackson.v1.models.TaskDebugStub
import me.neversleeps.api.jackson.v1.models.TaskPermission
import me.neversleeps.api.jackson.v1.models.TaskPriority
import me.neversleeps.api.jackson.v1.models.TaskStatus
import me.neversleeps.api.jackson.v1.models.TaskType
import me.neversleeps.api.jackson.v1.models.WorkMode
import me.neversleeps.common.TaskContext
import me.neversleeps.common.models.AppCommand
import me.neversleeps.common.models.AppError
import me.neversleeps.common.models.AppState
import me.neversleeps.common.models.RequestId
import me.neversleeps.common.models.task.Task
import me.neversleeps.common.models.task.TaskId
import me.neversleeps.common.models.user.UserId
import me.neversleeps.mappers.jackson.fromInternal.toTransport
import me.neversleeps.mappers.jackson.fromTransport.fromTransport
import org.junit.Test
import java.util.UUID
import kotlin.test.assertEquals

class TaskMapperTest {
    @Test
    fun fromTransport() {
        val request = TaskCreateRequest(
            requestType = "createTask",
            requestId = UUID.randomUUID().toString(),
            stub = TaskDebugStub.SUCCESS,
            mode = WorkMode.STUB,
            data = TaskCreateObject(
                type = TaskType.BACKEND,
                priority = TaskPriority.HIGH,
                status = TaskStatus.TODO,
                title = "some title",
                description = "some description",
                executor = UUID.randomUUID().toString(),
                createdBy = UUID.randomUUID().toString(),
            ),
        )

        val context = TaskContext()
        context.fromTransport(request)

        assertEquals(AppCommand.CREATE, context.command)
        assertEquals(me.neversleeps.common.stubs.TaskDebugStub.SUCCESS, context.stubCase)
        assertEquals(me.neversleeps.common.models.task.TaskType.BACKEND, context.taskRequest.type)
        assertEquals(me.neversleeps.common.models.task.TaskPriority.HIGH, context.taskRequest.priority)
        assertEquals(me.neversleeps.common.models.task.TaskStatus.TODO, context.taskRequest.status)
        assertEquals(request.data?.title, context.taskRequest.title)
        assertEquals(request.data?.description, context.taskRequest.description)
        assertEquals(request.data?.executor, context.taskRequest.executor.asString())
        assertEquals(request.data?.createdBy, context.taskRequest.createdBy.asString())
    }

    @Test
    fun toTransport() {
        val context = TaskContext(
            requestId = RequestId(UUID.randomUUID().toString()),
            command = AppCommand.CREATE,
            state = AppState.FINISHING,
            errors = mutableListOf(
                AppError(
                    code = "some code",
                    group = "some group",
                    field = "some field",
                    message = "some message",
                ),
            ),
            taskResponse = Task(
                id = TaskId(UUID.randomUUID().toString()),
                type = me.neversleeps.common.models.task.TaskType.FRONTEND,
                priority = me.neversleeps.common.models.task.TaskPriority.MEDIUM,
                status = me.neversleeps.common.models.task.TaskStatus.IN_PROGRESS,
                title = "some title",
                description = "some description",
                executor = UserId(UUID.randomUUID().toString()),
                createdBy = UserId(UUID.randomUUID().toString()),
                permissions = mutableSetOf(
                    me.neversleeps.common.models.task.TaskPermission.READ,
                    me.neversleeps.common.models.task.TaskPermission.DELETE,
                ),
            ),
        )

        val response = context.toTransport() as TaskCreateResponse

        assertEquals(context.requestId.asString(), response.requestId)
        assertEquals(ResponseResultStatus.SUCCESS, response.resultStatus)
        assertEquals("some code", response.errors?.get(0)?.code)
        assertEquals("some group", response.errors?.get(0)?.group)
        assertEquals("some field", response.errors?.get(0)?.field)
        assertEquals("some message", response.errors?.get(0)?.message)
        assertEquals(context.taskResponse.id.asString(), response.task?.id)
        assertEquals(TaskType.FRONTEND, response.task?.type)
        assertEquals(TaskPriority.MEDIUM, response.task?.priority)
        assertEquals(TaskStatus.IN_PROGRESS, response.task?.status)
        assertEquals(context.taskResponse.title, response.task?.title)
        assertEquals(context.taskResponse.description, response.task?.description)
        assertEquals(context.taskResponse.createdBy.asString(), response.task?.createdBy)
        assertEquals(mutableSetOf(TaskPermission.READ, TaskPermission.DELETE), response.task?.permissions)
    }
}
