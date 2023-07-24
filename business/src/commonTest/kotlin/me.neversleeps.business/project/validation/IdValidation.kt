package me.neversleeps.business.project.validation

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import me.neversleeps.business.ProjectProcessor
import me.neversleeps.common.ProjectContext
import me.neversleeps.common.models.AppCommand
import me.neversleeps.common.models.AppState
import me.neversleeps.common.models.project.Project
import me.neversleeps.common.models.project.ProjectId
import me.neversleeps.common.models.project.ProjectPermission
import kotlin.test.assertContains
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals

private val project = Project(
    id = ProjectId("123-234-abc-ABC"),
    title = "abc",
    description = "abc",
    permissions = mutableSetOf(ProjectPermission.READ, ProjectPermission.UPDATE),
)

@OptIn(ExperimentalCoroutinesApi::class)
fun validationIdCorrect(command: AppCommand, processor: ProjectProcessor) = runTest {
    val ctx = ProjectContext(
        command = command,
        state = AppState.NONE,
        projectRequest = project.copy(id = ProjectId("123-234-abc-ABC")),
    )

    processor.execute(ctx)

    assertEquals(0, ctx.errors.size)
    assertNotEquals(AppState.FAILING, ctx.state)
}

@OptIn(ExperimentalCoroutinesApi::class)
fun validationIdTrim(command: AppCommand, processor: ProjectProcessor) = runTest {
    val ctx = ProjectContext(
        command = command,
        state = AppState.NONE,
        projectRequest = project.copy(id = ProjectId(" \n\t 123-234-abc-ABC \n\t ")),
    )

    processor.execute(ctx)

    assertEquals(0, ctx.errors.size)
    assertNotEquals(AppState.FAILING, ctx.state)
}

@OptIn(ExperimentalCoroutinesApi::class)
fun validationIdEmpty(command: AppCommand, processor: ProjectProcessor) = runTest {
    val ctx = ProjectContext(
        command = command,
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
