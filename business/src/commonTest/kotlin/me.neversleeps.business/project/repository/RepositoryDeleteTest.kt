package me.neversleeps.business.project.repository

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import me.neversleeps.business.ProjectProcessor
import me.neversleeps.common.CorSettings
import me.neversleeps.common.ProjectContext
import me.neversleeps.common.models.AppCommand
import me.neversleeps.common.models.AppLock
import me.neversleeps.common.models.AppState
import me.neversleeps.common.models.project.Project
import me.neversleeps.common.models.project.ProjectId
import me.neversleeps.common.models.project.ProjectPermission
import me.neversleeps.common.models.user.UserId
import me.neversleeps.common.repository.project.DbProjectResponse
import me.neversleeps.common.stubs.WorkMode
import me.neversleeps.`in`.memory.project.ProjectRepositoryMock
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

@OptIn(ExperimentalCoroutinesApi::class)
class RepositoryDeleteTest {

    private val userId = UserId("321")
    private val command = AppCommand.DELETE
    private val initProject = Project(
        id = ProjectId("project-od"),
        title = "title",
        description = "description",
        createdBy = userId,
        permissions = mutableSetOf(ProjectPermission.UPDATE),
        lock = AppLock("123-234-abc-ABC"),
    )

    private val repo by lazy {
        ProjectRepositoryMock(
            invokeReadProject = {
                DbProjectResponse(
                    isSuccess = true,
                    data = initProject,
                )
            },
            invokeDeleteProject = {
                if (it.id == initProject.id) {
                    DbProjectResponse(
                        isSuccess = true,
                        data = initProject,
                    )
                } else {
                    DbProjectResponse(isSuccess = false, data = null)
                }
            },
        )
    }
    private val settings by lazy {
        CorSettings(
            repositoryTest = repo,
        )
    }
    private val processor by lazy { ProjectProcessor(settings) }

    @Test
    fun repoDeleteSuccessTest() = runTest {
        val projectRequest = Project(
            id = ProjectId("123"),
            lock = AppLock("123-234-abc-ABC"),
        )
        val ctx = ProjectContext(
            command = command,
            state = AppState.NONE,
            workMode = WorkMode.TEST,
            projectRequest = projectRequest,
        )
        processor.execute(ctx)
//        assertEquals(AppState.FINISHING, ctx.state)
        assertTrue { ctx.errors.isEmpty() }
        assertEquals(initProject.id, ctx.projectResponse.id)
        assertEquals(initProject.title, ctx.projectResponse.title)
        assertEquals(initProject.description, ctx.projectResponse.description)
    }

    @Test
    fun repoDeleteNotFoundTest() = repositoryNotFoundTest(command)
}
