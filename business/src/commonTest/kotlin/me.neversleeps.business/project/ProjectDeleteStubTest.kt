package me.neversleeps.business.project

import ProjectStub
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import me.neversleeps.business.ProjectProcessor
import me.neversleeps.common.ProjectContext
import me.neversleeps.common.models.AppCommand
import me.neversleeps.common.models.AppState
import me.neversleeps.common.models.project.Project
import me.neversleeps.common.models.project.ProjectId
import me.neversleeps.common.stubs.ProjectDebugStub
import kotlin.test.Test
import kotlin.test.assertContains
import kotlin.test.assertEquals

@OptIn(ExperimentalCoroutinesApi::class)
class ProjectDeleteStubTest {

    private val processor = ProjectProcessor()
    private val id = ProjectId("03e13b55-b5b2-484d-a08b-b18aeb087c88")

    @Test
    fun success() = runTest {
        // given
        val ctx = ProjectContext(
            command = AppCommand.DELETE,
            state = AppState.NONE,
            stubCase = ProjectDebugStub.SUCCESS,
            projectRequest = Project(
                id = id,
            ),
        )
        // when
        processor.execute(ctx)
        // then
        assertEquals(ProjectStub.get().id, ctx.projectResponse.id)
        assertEquals(ProjectStub.get().title, ctx.projectResponse.title)
        assertEquals(ProjectStub.get().description, ctx.projectResponse.description)
        assertEquals(ProjectStub.get().createdBy, ctx.projectResponse.createdBy)
        assertContains(ProjectStub.get().permissions, ctx.projectResponse.permissions.random(), "assert error")
    }

    @Test
    fun badId() = runTest {
        // given
        val ctx = ProjectContext(
            command = AppCommand.DELETE,
            state = AppState.NONE,
            stubCase = ProjectDebugStub.BAD_ID,
            projectRequest = Project(
                id = ProjectId(""),
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
    fun databaseError() = runTest {
        // given
        val ctx = ProjectContext(
            command = AppCommand.DELETE,
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
