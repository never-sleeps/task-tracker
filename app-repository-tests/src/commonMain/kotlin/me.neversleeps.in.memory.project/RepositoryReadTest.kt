package me.neversleeps.`in`.memory.project // ktlint-disable package-name

import kotlinx.coroutines.ExperimentalCoroutinesApi
import me.neversleeps.common.models.project.Project
import me.neversleeps.common.models.project.ProjectId
import me.neversleeps.common.repository.project.DbProjectIdRequest
import me.neversleeps.common.repository.project.IProjectRepository
import kotlin.test.Test
import kotlin.test.assertEquals


@OptIn(ExperimentalCoroutinesApi::class)
abstract class RepositoryReadTest {
    abstract val repo: IProjectRepository
    protected open val readSucc = initObjects[0]

    @Test
    fun readSuccess() = runRepoTest {
        val result = repo.readProject(DbProjectIdRequest(readSucc.id))

        assertEquals(true, result.isSuccess)
        assertEquals(readSucc, result.data)
        assertEquals(emptyList(), result.errors)
    }

    @Test
    fun readNotFound() = runRepoTest {
        val result = repo.readProject(DbProjectIdRequest(notFoundId))

        assertEquals(false, result.isSuccess)
        assertEquals(null, result.data)
        val error = result.errors.find { it.code == "not-found" }
        assertEquals("id", error?.field)
    }

    companion object : BaseInitProjects("delete") {
        override val initObjects: List<Project> = listOf(
            createInitTestModel("read"),
        )

        val notFoundId = ProjectId("ad-repo-read-notFound")
    }
}
