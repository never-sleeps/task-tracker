package me.neversleeps.business.project.stub

import ProjectStub
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import me.neversleeps.business.ProjectProcessor
import me.neversleeps.common.CorSettings
import me.neversleeps.common.ProjectContext
import me.neversleeps.common.models.AppCommand
import me.neversleeps.common.models.AppState
import me.neversleeps.common.models.project.Project
import me.neversleeps.common.models.project.ProjectId
import me.neversleeps.common.models.project.ProjectPermission
import me.neversleeps.common.models.user.UserId
import me.neversleeps.common.stubs.ProjectDebugStub
import kotlin.test.Test
import kotlin.test.assertContains
import kotlin.test.assertEquals

@OptIn(ExperimentalCoroutinesApi::class)
class ProjectCreateStubTest {

    private val processor = ProjectProcessor(CorSettings())
    private val id = ProjectId("03e13b55-b5b2-484d-a08b-b18aeb087c88")
    private val title = "title for PRO-001"
    private val description = "desc for PRO-001"
    private val createdBy = UserId("user-id-for-pro-001")
    private val permissions = mutableSetOf(ProjectPermission.UPDATE, ProjectPermission.DELETE, ProjectPermission.READ)

    @Test
    fun success() = runTest {
        // given
        val ctx = ProjectContext(
            command = AppCommand.CREATE,
            state = AppState.NONE,
            stubCase = ProjectDebugStub.SUCCESS,
            projectRequest = Project(
                id = id,
                title = title,
                description = description,
                createdBy = createdBy,
                permissions = permissions,
            ),
        )
        // when
        processor.execute(ctx)
        // then
        assertEquals(ProjectStub.get().id, ctx.projectResponse.id)
        assertEquals(title, ctx.projectResponse.title)
        assertEquals(description, ctx.projectResponse.description)
        assertEquals(createdBy, ctx.projectResponse.createdBy)
        assertContains(permissions, ctx.projectResponse.permissions.random(), "assert error")
    }

    @Test
    fun badId() = runTest {
        // given
        val ctx = ProjectContext(
            command = AppCommand.CREATE,
            state = AppState.NONE,
            stubCase = ProjectDebugStub.BAD_ID,
            projectRequest = Project(
                id = ProjectId(""),
                title = title,
                description = description,
                createdBy = createdBy,
                permissions = permissions,
            ),
        )
        // when
        processor.execute(ctx)
        // then
        assertEquals(Project(), ctx.projectResponse)
        assertEquals("id", ctx.errors.firstOrNull()?.field)
        assertEquals("validation", ctx.errors.firstOrNull()?.group)
    }

    @Test
    fun badTitle() = runTest {
        // given
        val ctx = ProjectContext(
            command = AppCommand.CREATE,
            state = AppState.NONE,
            stubCase = ProjectDebugStub.BAD_TITLE,
            projectRequest = Project(
                id = ProjectId(""),
                title = title,
                description = description,
                createdBy = createdBy,
                permissions = permissions,
            ),
        )
        // when
        processor.execute(ctx)
        // then
        assertEquals(Project(), ctx.projectResponse)
        assertEquals("title", ctx.errors.firstOrNull()?.field)
        assertEquals("validation", ctx.errors.firstOrNull()?.group)
    }

    @Test
    fun databaseError() = runTest {
        // given
        val ctx = ProjectContext(
            command = AppCommand.CREATE,
            state = AppState.NONE,
            stubCase = ProjectDebugStub.DB_ERROR,
            projectRequest = Project(),
        )
        // when
        processor.execute(ctx)
        // then
        assertEquals(Project(), ctx.projectResponse)
        assertEquals("internal-db", ctx.errors.firstOrNull()?.code)
        assertEquals("internal", ctx.errors.firstOrNull()?.group)
    }
}
