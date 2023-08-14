package me.neversleeps.`in`.memory.project

import kotlinx.coroutines.ExperimentalCoroutinesApi
import me.neversleeps.common.models.project.Project
import me.neversleeps.common.models.project.ProjectId
import me.neversleeps.common.models.project.ProjectPermission
import me.neversleeps.common.models.user.UserId
import me.neversleeps.common.repository.project.DbProjectRequest
import me.neversleeps.common.repository.project.IProjectRepository
import kotlin.test.Test
import kotlin.test.assertEquals


@OptIn(ExperimentalCoroutinesApi::class)
abstract class RepositoryUpdateTest {
    abstract val repo: IProjectRepository
    protected open val updateSuccess = initObjects[0]
    private val updateIdNotFound = ProjectId("ad-repo-update-not-found")

    private val reqUpdateSuccess by lazy {
        Project(
            id = updateSuccess.id,
            title = "update object",
            description = "update object description",
            createdBy = UserId("owner-123"),
            permissions = mutableSetOf(ProjectPermission.READ),
        )
    }
    private val reqUpdateNotFound = Project(
        id = updateIdNotFound,
        title = "update object not found",
        description = "update object not found description",
        createdBy = UserId("owner-123"),
        permissions = mutableSetOf(ProjectPermission.READ),
    )

    @Test
    fun updateSuccess() = runRepoTest {
        val result = repo.updateProject(DbProjectRequest(reqUpdateSuccess))
        assertEquals(true, result.isSuccess)
        assertEquals(reqUpdateSuccess.id, result.data?.id)
        assertEquals(reqUpdateSuccess.title, result.data?.title)
        assertEquals(reqUpdateSuccess.description, result.data?.description)
        assertEquals(emptyList(), result.errors)
    }

    @Test
    fun updateNotFound() = runRepoTest {
        val result = repo.updateProject(DbProjectRequest(reqUpdateNotFound))
        assertEquals(false, result.isSuccess)
        assertEquals(null, result.data)
        val error = result.errors.find { it.code == "not-found" }
        assertEquals("id", error?.field)
    }

    companion object : BaseInitProjects("update") {
        override val initObjects: List<Project> = listOf(
            createInitTestModel("update"),
            createInitTestModel("updateConc"),
        )
    }
}
