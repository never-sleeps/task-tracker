package me.neversleeps.mappers.jackson

import me.neversleeps.api.jackson.v1.models.ProjectCreateObject
import me.neversleeps.api.jackson.v1.models.ProjectCreateRequest
import me.neversleeps.api.jackson.v1.models.ProjectCreateResponse
import me.neversleeps.api.jackson.v1.models.ProjectDebugStub
import me.neversleeps.api.jackson.v1.models.ProjectPermission
import me.neversleeps.api.jackson.v1.models.ResponseResultStatus
import me.neversleeps.common.ProjectContext
import me.neversleeps.common.models.AppCommand
import me.neversleeps.common.models.AppError
import me.neversleeps.common.models.AppState
import me.neversleeps.common.models.RequestId
import me.neversleeps.common.models.project.Project
import me.neversleeps.common.models.project.ProjectId
import me.neversleeps.common.models.user.UserId
import me.neversleeps.mappers.jackson.fromInternal.toTransport
import me.neversleeps.mappers.jackson.fromTransport.fromTransport
import org.junit.Test
import java.util.UUID
import kotlin.test.assertEquals

class ProjectMapperTest {
    @Test
    fun fromTransport() {
        val request = ProjectCreateRequest(
            requestType = "createProject",
            requestId = UUID.randomUUID().toString(),
            stub = ProjectDebugStub.SUCCESS,
            data = ProjectCreateObject(
                title = "some title",
                description = "some description",
                createdBy = UUID.randomUUID().toString(),
            ),
        )

        val context = ProjectContext()
        context.fromTransport(request)

        assertEquals(AppCommand.CREATE, context.command)
        assertEquals(me.neversleeps.common.stubs.ProjectDebugStub.SUCCESS, context.stubCase)
        assertEquals(request.data?.title, context.projectRequest.title)
        assertEquals(request.data?.description, context.projectRequest.description)
        assertEquals(request.data?.createdBy, context.projectRequest.createdBy.asString())
    }

    @Test
    fun toTransport() {
        val context = ProjectContext(
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
            projectResponse = Project(
                id = ProjectId(UUID.randomUUID().toString()),
                title = "some title",
                description = "some description",
                createdBy = UserId(UUID.randomUUID().toString()),
                permissions = mutableSetOf(
                    me.neversleeps.common.models.project.ProjectPermission.READ,
                    me.neversleeps.common.models.project.ProjectPermission.UPDATE,
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
