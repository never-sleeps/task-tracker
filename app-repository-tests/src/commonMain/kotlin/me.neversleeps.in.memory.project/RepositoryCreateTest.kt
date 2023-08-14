package me.neversleeps.`in`.memory.project // ktlint-disable package-name

import kotlinx.coroutines.ExperimentalCoroutinesApi
import me.neversleeps.common.models.project.Project
import me.neversleeps.common.models.project.ProjectId
import me.neversleeps.common.models.project.ProjectPermission
import me.neversleeps.common.models.user.UserId
import me.neversleeps.common.repository.project.DbProjectRequest
import me.neversleeps.common.repository.project.IProjectRepository
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals

@OptIn(ExperimentalCoroutinesApi::class)
abstract class RepositoryCreateTest {
    abstract val repo: IProjectRepository

    private val createObj = Project(
        title = "create object",
        description = "create object description",
        createdBy = UserId("owner-123"),
        permissions = mutableSetOf(ProjectPermission.READ),
    )

    @Test
    fun createSuccess() = runRepoTest {
        val result = repo.createProject(DbProjectRequest(createObj))
        val expected = createObj.copy(id = result.data?.id ?: ProjectId.NONE)
        assertEquals(true, result.isSuccess)
        assertEquals(expected.title, result.data?.title)
        assertEquals(expected.description, result.data?.description)
        assertNotEquals(ProjectId.NONE, result.data?.id)
        assertEquals(emptyList(), result.errors)
    }

    companion object : BaseInitProjects("create") {
        override val initObjects: List<Project> = emptyList()
    }
}
