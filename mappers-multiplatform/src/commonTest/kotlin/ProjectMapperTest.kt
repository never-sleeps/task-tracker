import fromInternal.toTransport
import fromTransport.fromTransport
import me.neversleeps.api.multiplatform.v1.models.ProjectCreateObject
import me.neversleeps.api.multiplatform.v1.models.ProjectCreateRequest
import me.neversleeps.api.multiplatform.v1.models.ProjectCreateResponse
import me.neversleeps.api.multiplatform.v1.models.ProjectDebugStub
import me.neversleeps.api.multiplatform.v1.models.ProjectPermission
import me.neversleeps.api.multiplatform.v1.models.ResponseResultStatus
import models.AppCommand
import models.AppError
import models.AppState
import models.RequestId
import models.project.Project
import models.project.ProjectId
import models.user.UserId
import kotlin.test.Test
import kotlin.test.assertEquals

class ProjectMapperTest {
    @Test
    fun fromTransport() {
        val request = ProjectCreateRequest(
            requestType = "createProject",
            requestId = "request-id",
            stub = ProjectDebugStub.SUCCESS,
            data = ProjectCreateObject(
                title = "some title",
                description = "some description",
                createdBy = "created-by-id",
            ),
        )

        val context = ProjectContext()
        context.fromTransport(request)

        assertEquals(AppCommand.CREATE, context.command)
        assertEquals(stubs.ProjectDebugStub.SUCCESS, context.stubCase)
        assertEquals(request.data?.title, context.projectRequest.title)
        assertEquals(request.data?.description, context.projectRequest.description)
        assertEquals(request.data?.createdBy, context.projectRequest.createdBy.asString())
    }

    @Test
    fun toTransport() {
        val context = ProjectContext(
            requestId = RequestId("request-id"),
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
            projectResponse = Project(
                id = ProjectId("project-id"),
                title = "some title",
                description = "some description",
                createdBy = UserId("created-by-id"),
                permissions = mutableSetOf(
                    models.project.ProjectPermission.READ,
                    models.project.ProjectPermission.UPDATE,
                ),
            ),
        )

        val response = context.toTransport() as ProjectCreateResponse

        assertEquals(context.requestId.asString(), response.requestId)
        assertEquals(ResponseResultStatus.SUCCESS, response.resultStatus)
        assertEquals("some code", response.errors?.get(0)?.code)
        assertEquals("some group", response.errors?.get(0)?.group)
        assertEquals("some field", response.errors?.get(0)?.field)
        assertEquals("some message", response.errors?.get(0)?.message)
        assertEquals(context.projectResponse.id.asString(), response.project?.id)
        assertEquals(context.projectResponse.title, response.project?.title)
        assertEquals(context.projectResponse.description, response.project?.description)
        assertEquals(context.projectResponse.createdBy.asString(), response.project?.createdBy)
        assertEquals(mutableSetOf(ProjectPermission.READ, ProjectPermission.UPDATE), response.project?.permissions)
    }
}
