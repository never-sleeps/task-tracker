package me.neversleeps.business.project.validation

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import me.neversleeps.business.ProjectProcessor
import me.neversleeps.common.ProjectContext
import me.neversleeps.common.models.AppCommand
import me.neversleeps.common.models.AppState
import me.neversleeps.common.models.project.Project
import me.neversleeps.common.models.project.ProjectPermission
import kotlin.test.assertContains
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals

private val stub = ProjectStub.get()

private val project = Project(
    id = stub.id,
    title = "abc",
    description = "abc",
    permissions = mutableSetOf(ProjectPermission.READ, ProjectPermission.UPDATE),
)

@OptIn(ExperimentalCoroutinesApi::class)
fun validationTitleCorrect(command: AppCommand, processor: ProjectProcessor) = runTest {
    val ctx = ProjectContext(
        command = command,
        state = AppState.NONE,
        projectRequest = project.copy(title = "abc"),
    )

    processor.execute(ctx)

    assertEquals(0, ctx.errors.size)
    assertNotEquals(AppState.FAILING, ctx.state)
    assertEquals("abc", ctx.projectValidated.title)
}

@OptIn(ExperimentalCoroutinesApi::class)
fun validationTitleTrim(command: AppCommand, processor: ProjectProcessor) = runTest {
    val ctx = ProjectContext(
        command = command,
        state = AppState.NONE,
        projectRequest = project.copy(title = " \n\t abc \t\n "),
    )

    processor.execute(ctx)

    assertEquals(0, ctx.errors.size)
    assertNotEquals(AppState.FAILING, ctx.state)
    assertEquals("abc", ctx.projectValidated.title)
}

@OptIn(ExperimentalCoroutinesApi::class)
fun validationTitleEmpty(command: AppCommand, processor: ProjectProcessor) = runTest {
    val ctx = ProjectContext(
        command = command,
        state = AppState.NONE,
        projectRequest = project.copy(title = ""),
    )

    processor.execute(ctx)

    assertEquals(1, ctx.errors.size)
    assertEquals(AppState.FAILING, ctx.state)

    val error = ctx.errors.firstOrNull()
    assertEquals("title", error?.field)
    assertContains(error?.message ?: "", "title")
}

@OptIn(ExperimentalCoroutinesApi::class)
fun validationTitleSymbols(command: AppCommand, processor: ProjectProcessor) = runTest {
    val ctx = ProjectContext(
        command = command,
        state = AppState.NONE,
        projectRequest = project.copy(title = "!@#$%^&*(),.{}"),
    )

    processor.execute(ctx)

    assertEquals(1, ctx.errors.size)
    assertEquals(AppState.FAILING, ctx.state)

    val error = ctx.errors.firstOrNull()
    assertEquals("title", error?.field)
    assertContains(error?.message ?: "", "title")
}