package me.neversleeps.business.project.validation

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import me.neversleeps.business.ProjectProcessor
import me.neversleeps.business.project.addTestPrincipal
import me.neversleeps.common.ProjectContext
import me.neversleeps.common.models.AppCommand
import me.neversleeps.common.models.AppLock
import me.neversleeps.common.models.AppState
import me.neversleeps.common.models.project.Project
import me.neversleeps.common.models.project.ProjectId
import me.neversleeps.common.models.project.ProjectPermission
import me.neversleeps.common.stubs.WorkMode
import kotlin.test.assertContains
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals

private val stub = ProjectStub.prepareResult { id = ProjectId("123-234-abc-ABC") }

private val project = Project(
    id = ProjectId("123-234-abc-ABC"),
    title = "abc",
    description = "abc",
    permissions = mutableSetOf(ProjectPermission.READ, ProjectPermission.UPDATE),
    lock = AppLock("12345"),
)

@OptIn(ExperimentalCoroutinesApi::class)
fun validationIdCorrect(command: AppCommand, processor: ProjectProcessor) = runTest {
    val ctx = ProjectContext(
        command = command,
        workMode = WorkMode.STUB,
        state = AppState.NONE,
        projectRequest = project.copy(id = ProjectId("123-234-abc-ABC")),
    )

    ctx.addTestPrincipal(stub.createdBy)
    processor.execute(ctx)

    assertEquals(0, ctx.errors.size)
    assertNotEquals(AppState.FAILING, ctx.state)
}

@OptIn(ExperimentalCoroutinesApi::class)
fun validationIdTrim(command: AppCommand, processor: ProjectProcessor) = runTest {
    val ctx = ProjectContext(
        command = command,
        workMode = WorkMode.STUB,
        state = AppState.NONE,
        projectRequest = project.copy(id = ProjectId(" \n\t 123-234-abc-ABC \n\t ")),
    )

    ctx.addTestPrincipal(stub.createdBy)
    processor.execute(ctx)

    assertEquals(0, ctx.errors.size)
    assertNotEquals(AppState.FAILING, ctx.state)
}

@OptIn(ExperimentalCoroutinesApi::class)
fun validationIdEmpty(command: AppCommand, processor: ProjectProcessor) = runTest {
    val ctx = ProjectContext(
        command = command,
        workMode = WorkMode.STUB,
        state = AppState.NONE,
        projectRequest = project.copy(id = ProjectId("")),
    )

    processor.execute(ctx)

    assertEquals(1, ctx.errors.size)
    assertEquals(AppState.FAILING, ctx.state)
    val error = ctx.errors.firstOrNull()
    assertEquals("id", error?.field)
    assertContains(error?.message ?: "", "id")
}

@OptIn(ExperimentalCoroutinesApi::class)
fun validationIdFormat(command: AppCommand, processor: ProjectProcessor) = runTest {
    val ctx = ProjectContext(
        command = command,
        workMode = WorkMode.STUB,
        state = AppState.NONE,
        projectRequest = project.copy(id = ProjectId("!@#\$%^&*(),.{}")),
    )

    processor.execute(ctx)

    assertEquals(1, ctx.errors.size)
    assertEquals(AppState.FAILING, ctx.state)

    val error = ctx.errors.firstOrNull()
    assertEquals("id", error?.field)
    assertContains(error?.message ?: "", "id")
}
