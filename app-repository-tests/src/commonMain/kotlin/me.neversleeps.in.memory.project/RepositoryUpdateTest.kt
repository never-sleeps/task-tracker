package me.neversleeps.`in`.memory.project

import kotlinx.coroutines.ExperimentalCoroutinesApi
import me.neversleeps.common.models.AppLock
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
    protected open val updateConcurrent = initObjects[1]
    private val updateIdNotFound = ProjectId("ad-repo-update-not-found")
    protected val lockBad = AppLock("20000000-0000-0000-0000-000000000009")
    protected val lockNew = AppLock("20000000-0000-0000-0000-000000000002")

    private val reqUpdateSuccess by lazy {
        Project(
            id = updateSuccess.id,
            title = "update object",
            description = "update object description",
            createdBy = UserId("owner-123"),
            permissions = mutableSetOf(ProjectPermission.READ),
            lock = initObjects.first().lock,
        )
    }
    private val reqUpdateNotFound = Project(
        id = updateIdNotFound,
        title = "update object not found",
        description = "update object not found description",
        createdBy = UserId("owner-123"),
        permissions = mutableSetOf(ProjectPermission.READ),
        lock = initObjects.first().lock,
    )
    private val reqUpdateConcurrency = Project(
        id = updateConcurrent.id,
        title = "update object not found",
        description = "update object not found description",
        createdBy = UserId("owner-123"),
        permissions = mutableSetOf(ProjectPermission.READ),
        lock = lockBad,
    )

    @Test
    fun updateSuccess() = runRepoTest {
        val result = repo.updateProject(DbProjectRequest(reqUpdateSuccess))
        assertEquals(true, result.isSuccess)
        assertEquals(reqUpdateSuccess.id, result.data?.id)
        assertEquals(reqUpdateSuccess.title, result.data?.title)
        assertEquals(reqUpdateSuccess.description, result.data?.description)
        assertEquals(emptyList(), result.errors)
        assertEquals(lockNew, result.data?.lock)
    }

    @Test
    fun updateNotFound() = runRepoTest {
        val result = repo.updateProject(DbProjectRequest(reqUpdateNotFound))
        assertEquals(false, result.isSuccess)
        assertEquals(null, result.data)
        val error = result.errors.find { it.code == "not-found" }
        assertEquals("id", error?.field)
    }

    @Test
    fun updateConcurrencyError() = runRepoTest {
        val result = repo.updateProject(DbProjectRequest(reqUpdateConcurrency))
        assertEquals(false, result.isSuccess)
        val error = result.errors.find { it.code == "concurrency" }
        assertEquals("lock", error?.field)
        assertEquals(updateConcurrent, result.data)
    }

    companion object : BaseInitProjects("update") {
        override val initObjects: List<Project> = listOf(
            createInitTestModel("update"),
            createInitTestModel("updateConc"),
        )
    }
}
