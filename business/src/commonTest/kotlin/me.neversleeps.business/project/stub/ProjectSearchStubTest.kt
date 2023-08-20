package me.neversleeps.business.project.stub

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import me.neversleeps.business.ProjectProcessor
import me.neversleeps.common.CorSettings
import me.neversleeps.common.ProjectContext
import me.neversleeps.common.models.AppCommand
import me.neversleeps.common.models.AppState
import me.neversleeps.common.models.project.Project
import me.neversleeps.common.models.project.ProjectSearchFilter
import me.neversleeps.common.models.user.UserId
import me.neversleeps.common.stubs.ProjectDebugStub
import me.neversleeps.common.stubs.WorkMode
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import kotlin.test.fail

@OptIn(ExperimentalCoroutinesApi::class)
class ProjectSearchStubTest {

    private val processor = ProjectProcessor(CorSettings())
    private val createdBy = UserId("user-id")
    private val searchText = "random important text"
    private val filter = ProjectSearchFilter(
        searchText = searchText,
        createdBy = createdBy,
    )

    @Test
    fun success() = runTest {
        // given
        val ctx = ProjectContext(
            command = AppCommand.SEARCH,
            state = AppState.NONE,
            stubCase = ProjectDebugStub.SUCCESS,
            workMode = WorkMode.STUB,
            projectSearchFilterRequest = filter,
        )
        // when
        processor.execute(ctx)
        // then
        assertTrue(ctx.projectsResponse.size > 0)
        val first = ctx.projectsResponse.firstOrNull() ?: fail("Empty response list")
        assertTrue(first.title.contains(searchText))
        assertTrue(first.description.contains(searchText))
    }

    @Test
    fun badId() = runTest {
        // given
        val ctx = ProjectContext(
            command = AppCommand.SEARCH,
            state = AppState.NONE,
            stubCase = ProjectDebugStub.BAD_ID,
            workMode = WorkMode.STUB,
            projectSearchFilterRequest = filter,
        )
        // when
        processor.execute(ctx)
        // then
        assertEquals(Project(), ctx.projectResponse)
        assertEquals("id", ctx.errors.firstOrNull()?.field)
        assertEquals("validation", ctx.errors.firstOrNull()?.group)
    }

    @Test
    fun badSearchText() = runTest {
        // given
        val ctx = ProjectContext(
            command = AppCommand.SEARCH,
            state = AppState.NONE,
            stubCase = ProjectDebugStub.BAD_SEARCH_TEXT,
            workMode = WorkMode.STUB,
            projectSearchFilterRequest = filter,
        )
        // when
        processor.execute(ctx)
        // then
        assertEquals(Project(), ctx.projectResponse)
        assertEquals("searchText", ctx.errors.firstOrNull()?.field)
        assertEquals("validation", ctx.errors.firstOrNull()?.group)
    }

    @Test
    fun badSearchCreatedBy() = runTest {
        // given
        val ctx = ProjectContext(
            command = AppCommand.SEARCH,
            state = AppState.NONE,
            stubCase = ProjectDebugStub.BAD_SEARCH_CREATED_BY,
            workMode = WorkMode.STUB,
            projectSearchFilterRequest = filter,
        )
        // when
        processor.execute(ctx)
        // then
        assertEquals(Project(), ctx.projectResponse)
        assertEquals("searchCreatedBy", ctx.errors.firstOrNull()?.field)
        assertEquals("validation", ctx.errors.firstOrNull()?.group)
    }

    @Test
    fun databaseError() = runTest {
        // given
        val ctx = ProjectContext(
            command = AppCommand.SEARCH,
            state = AppState.NONE,
            stubCase = ProjectDebugStub.DB_ERROR,
            workMode = WorkMode.STUB,
            projectSearchFilterRequest = filter,
        )
        // when
        processor.execute(ctx)
        // then
        assertEquals(Project(), ctx.projectResponse)
        assertEquals("internal-db", ctx.errors.firstOrNull()?.code)
        assertEquals("internal", ctx.errors.firstOrNull()?.group)
    }
}
