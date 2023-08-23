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
    protected open val deleteSuccess = initObjects[0]
    protected open val deleteConcurrent = initObjects[1]
    protected open val notFoundId = ProjectId("project-repo-delete-notFound")

    @Test
    fun deleteSuccess() = runRepoTest {
        val lockOld = deleteSuccess.lock
        val result = repo.deleteProject(DbProjectIdRequest(deleteSuccess.id, lock = lockOld))

        assertEquals(true, result.isSuccess)
        assertEquals(emptyList(), result.errors)
        assertEquals(lockOld, result.data?.lock)
    }

    @Test
    fun deleteNotFound() = runRepoTest {
        val result = repo.readProject(DbProjectIdRequest(notFoundId, lock = lockOld))

        assertEquals(false, result.isSuccess)
        assertEquals(null, result.data)
        val error = result.errors.find { it.code == "not-found" }
        assertEquals("id", error?.field)
    }

    @Test
    fun deleteConcurrency() = runRepoTest {
        val lockOld = deleteSuccess.lock
        val result = repo.deleteProject(DbProjectIdRequest(deleteConcurrent.id, lock = lockBad))

        assertEquals(false, result.isSuccess)
        val error = result.errors.find { it.code == "concurrency" }
        assertEquals("lock", error?.field)
        assertEquals(lockOld, result.data?.lock)
    }

    companion object : BaseInitProjects("delete") {
        override val initObjects: List<Project> = listOf(
            createInitTestModel("delete"),
            createInitTestModel("deleteLock"),
        )
    }
}
