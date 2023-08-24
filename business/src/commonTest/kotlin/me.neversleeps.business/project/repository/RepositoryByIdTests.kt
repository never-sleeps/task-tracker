package me.neversleeps.business.project.repository

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import me.neversleeps.business.ProjectProcessor
import me.neversleeps.common.CorSettings
import me.neversleeps.common.ProjectContext
import me.neversleeps.common.models.AppCommand
import me.neversleeps.common.models.AppError
import me.neversleeps.common.models.AppLock
import me.neversleeps.common.models.AppState
import me.neversleeps.common.models.project.Project
import me.neversleeps.common.models.project.ProjectId
import me.neversleeps.common.models.project.ProjectPermission
import me.neversleeps.common.models.user.UserId
import me.neversleeps.common.repository.project.DbProjectResponse
import me.neversleeps.common.stubs.WorkMode
import me.neversleeps.`in`.memory.project.ProjectRepositoryMock
import me.neversleeps.business.project.addTestPrincipal
import kotlin.test.assertEquals

private val userId = UserId("321")
private val initProject = Project(
    id = ProjectId("project-od"),
    title = "title",
    description = "description",
    createdBy = userId,
    permissions = mutableSetOf(ProjectPermission.UPDATE),
    lock = AppLock("123-234-abc-ABC"),
)

private val repo = ProjectRepositoryMock(
    invokeReadProject = {
        if (it.id == initProject.id) {
            DbProjectResponse(
                isSuccess = true,
                data = initProject,
            )
        } else {
            DbProjectResponse(
                isSuccess = false,
                data = null,
                errors = listOf(AppError(message = "Not found", field = "id")),
            )
        }
    },
)
private val settings by lazy {
    CorSettings(
        repositoryTest = repo,
    )
}
private val processor by lazy { ProjectProcessor(settings) }

@OptIn(ExperimentalCoroutinesApi::class)
fun repositoryNotFoundTest(command: AppCommand) = runTest {
    val ctx = ProjectContext(
        command = command,
        state = AppState.NONE,
        workMode = WorkMode.TEST,
        projectRequest = Project(
            id = ProjectId("12345"),
            title = "xyz",
            description = "xyz",
            lock = AppLock("123-234-abc-ABC"),
        ),
    )
    ctx.addTestPrincipal(userId)
    processor.execute(ctx)
    assertEquals(AppState.FAILING, ctx.state)
    assertEquals(Project(), ctx.projectResponse)
    assertEquals(1, ctx.errors.size)
    assertEquals("id", ctx.errors.first().field)
}
