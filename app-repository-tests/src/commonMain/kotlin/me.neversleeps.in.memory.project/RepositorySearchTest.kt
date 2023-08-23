package me.neversleeps.`in`.memory.project // ktlint-disable package-name

import kotlinx.coroutines.ExperimentalCoroutinesApi
import me.neversleeps.common.models.project.Project
import me.neversleeps.common.models.user.UserId
import me.neversleeps.common.repository.project.DbProjectFilterRequest
import me.neversleeps.common.repository.project.IProjectRepository
import kotlin.test.Test
import kotlin.test.assertEquals

@OptIn(ExperimentalCoroutinesApi::class)
abstract class RepositorySearchTest {
    abstract val repo: IProjectRepository

    protected open val initializedObjects: List<Project> = initObjects

    @Test
    fun searchOwner() = runRepoTest {
        val result = repo.searchProjects(DbProjectFilterRequest(createdBy = searchCreatedBy))
        assertEquals(true, result.isSuccess)
        val expected = listOf(initializedObjects[1], initializedObjects[2]).sortedBy { it.id.asString() }
        assertEquals(expected, result.data?.sortedBy { it.id.asString() })
        assertEquals(emptyList(), result.errors)
    }

    companion object : BaseInitProjects("search") {
        val searchCreatedBy = UserId("owner-124")
        override val initObjects: List<Project> = listOf(
            createInitTestModel("project1"),
            createInitTestModel("project2", createdBy = searchCreatedBy),
            createInitTestModel("project3", createdBy = searchCreatedBy),
        )
    }
}
