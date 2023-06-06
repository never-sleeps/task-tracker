import fromInternal.toTransport
import fromTransport.fromTransport
import me.neversleeps.api.jackson.v1.models.ResponseResultStatus
import me.neversleeps.api.jackson.v1.models.TaskCreateObject
import me.neversleeps.api.jackson.v1.models.TaskCreateRequest
import me.neversleeps.api.jackson.v1.models.TaskCreateResponse
import me.neversleeps.api.jackson.v1.models.TaskDebugStub
import me.neversleeps.api.jackson.v1.models.TaskPermission
import me.neversleeps.api.jackson.v1.models.TaskPriority
import me.neversleeps.api.jackson.v1.models.TaskStatus
import me.neversleeps.api.jackson.v1.models.TaskType
import models.AppCommand
import models.AppError
import models.AppState
import models.RequestId
import models.task.Task
import models.task.TaskId
import models.user.UserId
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
        assertEquals(stubs.TaskDebugStub.SUCCESS, context.stubCase)
        assertEquals(models.task.TaskType.BACKEND, context.taskRequest.type)
        assertEquals(models.task.TaskPriority.HIGH, context.taskRequest.priority)
        assertEquals(models.task.TaskStatus.TODO, context.taskRequest.status)
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
            state = AppState.RUNNING,
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
                type = models.task.TaskType.FRONTEND,
                priority = models.task.TaskPriority.MEDIUM,
                status = models.task.TaskStatus.IN_PROGRESS,
                title = "some title",
                description = "some description",
                executor = UserId(UUID.randomUUID().toString()),
                createdBy = UserId(UUID.randomUUID().toString()),
                permissions = mutableSetOf(
                    models.task.TaskPermission.READ,
                    models.task.TaskPermission.DELETE,
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
