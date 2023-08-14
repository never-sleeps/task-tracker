package me.neversleeps.`in`.memory.project // ktlint-disable package-name

import kotlinx.coroutines.ExperimentalCoroutinesApi
import me.neversleeps.common.models.project.Project
import me.neversleeps.common.models.project.ProjectId
import me.neversleeps.common.repository.project.DbProjectIdRequest
import me.neversleeps.common.repository.project.IProjectRepository
import kotlin.test.Test
import kotlin.test.assertEquals

@OptIn(ExperimentalCoroutinesApi::class)
abstract class RepositoryDeleteTest {
    abstract val repo: IProjectRepository
    protected open val deleteSucc = initObjects[0]

    @Test
    fun deleteSuccess() = runRepoTest {
        val result = repo.deleteProject(DbProjectIdRequest(deleteSucc.id))

        assertEquals(true, result.isSuccess)
        assertEquals(emptyList(), result.errors)
    }

    @Test
    fun deleteNotFound() = runRepoTest {
        val result = repo.readProject(DbProjectIdRequest(notFoundId))

        assertEquals(false, result.isSuccess)
        assertEquals(null, result.data)
        val error = result.errors.find { it.code == "not-found" }
        assertEquals("id", error?.field)
    }

    companion object : BaseInitProjects("delete") {
        override val initObjects: List<Project> = listOf(
            createInitTestModel("delete"),
            createInitTestModel("deleteLock"),
        )
        val notFoundId = ProjectId("ad-repo-delete-notFound")
    }
}
