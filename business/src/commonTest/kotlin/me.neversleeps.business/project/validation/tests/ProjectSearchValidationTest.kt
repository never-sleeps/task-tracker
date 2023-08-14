package me.neversleeps.business.project.validation.tests

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import me.neversleeps.business.ProjectProcessor
import me.neversleeps.common.CorSettings
import me.neversleeps.common.ProjectContext
import me.neversleeps.common.models.AppCommand
import me.neversleeps.common.models.AppState
import me.neversleeps.common.models.project.ProjectSearchFilter
import me.neversleeps.`in`.memory.project.ProjectRepositoryInMemory
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals

@OptIn(ExperimentalCoroutinesApi::class)
class ProjectSearchValidationTest {

    private val command = AppCommand.SEARCH
    private lateinit var processor: ProjectProcessor

    @BeforeTest
    fun beforeEach() {
        val repositoryTest = ProjectRepositoryInMemory(initObjects = listOf(ProjectStub.get()))
        processor = ProjectProcessor(CorSettings(repositoryTest = repositoryTest))
    }

    @Test
    fun correctEmpty() = runTest {
        val ctx = ProjectContext(
            command = command,
            state = AppState.NONE,
            projectSearchFilterRequest = ProjectSearchFilter(),
        )

        processor.execute(ctx)

        assertEquals(0, ctx.errors.size)
        assertNotEquals(AppState.FAILING, ctx.state)
    }
}
