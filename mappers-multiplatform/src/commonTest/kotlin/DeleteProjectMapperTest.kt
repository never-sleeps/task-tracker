package me.neversleeps.mappers.multiplatform

import me.neversleeps.api.multiplatform.v1.models.ProjectDebugStub
import me.neversleeps.api.multiplatform.v1.models.ProjectDeleteRequest
import me.neversleeps.api.multiplatform.v1.models.ProjectDeleteResponse
import me.neversleeps.common.ProjectContext
import me.neversleeps.common.models.AppCommand
import me.neversleeps.common.models.AppError
import me.neversleeps.common.models.AppLock
import me.neversleeps.common.models.AppState
import me.neversleeps.common.models.RequestId
import me.neversleeps.common.models.project.Project
import me.neversleeps.common.models.project.ProjectId
import me.neversleeps.common.models.project.ProjectPermission
import me.neversleeps.common.models.user.UserId
import me.neversleeps.common.stubs.WorkMode
import me.neversleeps.mappers.multiplatform.fromInternal.toTransportDelete
import me.neversleeps.mappers.multiplatform.fromTransport.fromTransport
import kotlin.test.Test
import kotlin.test.assertEquals

class DeleteProjectMapperTest {
    @Test
    fun fromTransport() {
        val requestObj = ProjectDeleteRequest(
            requestId = "12345",
            id = "03e13b55-b5b2-484d-a08b-b18aeb087c88",
            stub = ProjectDebugStub.SUCCESS,
            mode = me.neversleeps.api.multiplatform.v1.models.WorkMode.STUB,
            lock = "456789",
        )

        val context = ProjectContext()
        context.fromTransport(requestObj)

        assertEquals(me.neversleeps.common.stubs.ProjectDebugStub.SUCCESS, context.stubCase)
        assertEquals(WorkMode.STUB, context.workMode)
        assertEquals("03e13b55-b5b2-484d-a08b-b18aeb087c88", context.projectRequest.id.asString())
        assertEquals("456789", context.projectRequest.lock.asString())
    }

    @Test
    fun toTransport() {
        val context = ProjectContext(
            requestId = RequestId("1234"),
            command = AppCommand.DELETE,
            projectResponse = Project(
                id = ProjectId("12345"),
                title = "title",
                description = "desc",
                createdBy = UserId("owner-123"),
                permissions = mutableSetOf(ProjectPermission.READ),
                lock = AppLock("456789"),
            ),
            errors = mutableListOf(
                AppError(
                    code = "err",
                    group = "request",
                    field = "title",
                    message = "wrong title",
                ),
            ),
            state = AppState.RUNNING,
        )

        val req = context.toTransportDelete() as ProjectDeleteResponse

        assertEquals("1234", req.requestId)
        assertEquals("12345", req.project?.id)
        assertEquals("456789", req.project?.lock)
        assertEquals("title", req.project?.title)
        assertEquals("desc", req.project?.description)
        assertEquals("owner-123", req.project?.createdBy)
        assertEquals(1, req.errors?.size)
        assertEquals("err", req.errors?.firstOrNull()?.code)
        assertEquals("request", req.errors?.firstOrNull()?.group)
        assertEquals("title", req.errors?.firstOrNull()?.field)
        assertEquals("wrong title", req.errors?.firstOrNull()?.message)
    }
}
