package me.neversleeps.`in`.memory.project // ktlint-disable package-name

import com.benasher44.uuid.uuid4
import io.github.reactivecircus.cache4k.Cache
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import me.neversleeps.common.helpers.errorRepositoryConcurrency
import me.neversleeps.common.models.AppError
import me.neversleeps.common.models.AppLock
import me.neversleeps.common.models.project.Project
import me.neversleeps.common.models.project.ProjectId
import me.neversleeps.common.models.user.UserId
import me.neversleeps.common.repository.project.DbProjectFilterRequest
import me.neversleeps.common.repository.project.DbProjectIdRequest
import me.neversleeps.common.repository.project.DbProjectRequest
import me.neversleeps.common.repository.project.DbProjectResponse
import me.neversleeps.common.repository.project.DbProjectsResponse
import me.neversleeps.common.repository.project.IProjectRepository
import me.neversleeps.`in`.memory.project.entity.ProjectEntity
import kotlin.time.Duration
import kotlin.time.Duration.Companion.minutes

data class ProjectRepositoryInMemory(
    val initObjects: List<Project> = emptyList(),
    val ttl: Duration = 2.minutes,
    val randomUuid: () -> String = { uuid4().toString() },
) : IProjectRepository {

    private val mutex: Mutex = Mutex()

    private val cache = Cache.Builder<String, ProjectEntity>()
        .expireAfterWrite(ttl)
        .build()

    init {
        initObjects.forEach {
            save(it)
        }
    }

    private fun save(project: Project) {
        val entity = ProjectEntity(project)
        if (entity.id == null) {
            return
        }
        cache.put(entity.id!!, entity)
    }

    override suspend fun createProject(request: DbProjectRequest): DbProjectResponse {
        val key = randomUuid()
        val project = request.project.copy(id = ProjectId(key), lock = AppLock(randomUuid()))
        val entity = ProjectEntity(project)
        cache.put(key, entity)
        return DbProjectResponse(
            data = project,
            isSuccess = true,
        )
    }

    override suspend fun readProject(request: DbProjectIdRequest): DbProjectResponse {
        val key = request.id.takeIf { it != ProjectId.NONE }?.asString() ?: return resultErrorEmptyId
        return cache.get(key)
            ?.let {
                DbProjectResponse(
                    data = it.toInternal(),
                    isSuccess = true,
                )
            } ?: resultErrorNotFound
    }

    override suspend fun updateProject(request: DbProjectRequest): DbProjectResponse {
        val key = request.project.id.takeIf { it != ProjectId.NONE }?.asString() ?: return resultErrorEmptyId
        val oldLock = request.project.lock.takeIf { it != AppLock.NONE }?.asString() ?: return resultErrorEmptyLock
        val newProject = request.project.copy(lock = AppLock(randomUuid()))
        val entity = ProjectEntity(newProject)
        return mutex.withLock {
            val oldProject = cache.get(key)
            when {
                oldProject == null -> resultErrorNotFound
                oldProject.lock != oldLock -> DbProjectResponse(
                    data = oldProject.toInternal(),
                    isSuccess = false,
                    errors = listOf(
                        errorRepositoryConcurrency(
                            AppLock(oldLock),
                            oldProject.lock?.let { AppLock(it) },
                        ),
                    ),
                )

                else -> {
                    cache.put(key, entity)
                    DbProjectResponse(
                        data = newProject,
                        isSuccess = true,
                    )
                }
            }
        }
    }

    override suspend fun deleteProject(request: DbProjectIdRequest): DbProjectResponse {
        val key = request.id.takeIf { it != ProjectId.NONE }?.asString() ?: return resultErrorEmptyId
        val oldLock = request.lock.takeIf { it != AppLock.NONE }?.asString() ?: return resultErrorEmptyLock
        return mutex.withLock {
            val oldProject = cache.get(key)
            when {
                oldProject == null -> resultErrorNotFound
                oldProject.lock != oldLock -> DbProjectResponse(
                    data = oldProject.toInternal(),
                    isSuccess = false,
                    errors = listOf(
                        errorRepositoryConcurrency(
                            AppLock(oldLock),
                            oldProject.lock?.let { AppLock(it) },
                        ),
                    ),
                )

                else -> {
                    cache.invalidate(key)
                    DbProjectResponse(
                        data = oldProject.toInternal(),
                        isSuccess = true,
                    )
                }
            }
        }
    }

    /**
     * Поиск по фильтру.
     * Если в фильтре не установлен какой-либо из параметров - по нему фильтрация не идет
     */
    override suspend fun searchProjects(request: DbProjectFilterRequest): DbProjectsResponse {
        val result = cache.asMap().asSequence()
            .filter { entry ->
                request.createdBy.takeIf { it != UserId.NONE }?.let {
                    it.asString() == entry.value.createdBy
                } ?: true
            }
            .filter { entry ->
                request.searchText.takeIf { it.isNotBlank() }?.let {
                    entry.value.title?.contains(it) ?: false
                } ?: true
            }
            .map { it.value.toInternal() }
            .toList()
        return DbProjectsResponse(
            data = result,
            isSuccess = true,
        )
    }

    companion object {
        val resultErrorEmptyId = DbProjectResponse(
            data = null,
            isSuccess = false,
            errors = listOf(
                AppError(
                    code = "id-empty",
                    group = "validation",
                    field = "id",
                    message = "Id must not be null or blank",
                ),
            ),
        )
        val resultErrorNotFound = DbProjectResponse(
            isSuccess = false,
            data = null,
            errors = listOf(
                AppError(
                    code = "not-found",
                    field = "id",
                    message = "Not Found",
                ),
            ),
        )
        val resultErrorEmptyLock = DbProjectResponse(
            data = null,
            isSuccess = false,
            errors = listOf(
                AppError(
                    code = "lock-empty",
                    group = "validation",
                    field = "lock",
                    message = "Lock must not be null or blank",
                ),
            ),
        )
    }
}
