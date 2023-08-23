package me.neversleeps.repository.gremlin

import com.benasher44.uuid.uuid4
import me.neversleeps.common.helpers.asAppError
import me.neversleeps.common.helpers.errorAdministration
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
import me.neversleeps.repository.gremlin.ProjectGremlinConst.FIELD_CREATED_BY
import me.neversleeps.repository.gremlin.ProjectGremlinConst.FIELD_LOCK
import me.neversleeps.repository.gremlin.ProjectGremlinConst.FIELD_TITLE
import me.neversleeps.repository.gremlin.ProjectGremlinConst.FIELD_TMP_RESULT
import me.neversleeps.repository.gremlin.ProjectGremlinConst.RESULT_LOCK_FAILURE
import me.neversleeps.repository.gremlin.exceptions.DuplicatedDbElementsException
import me.neversleeps.repository.gremlin.mappers.addProject
import me.neversleeps.repository.gremlin.mappers.label
import me.neversleeps.repository.gremlin.mappers.projectList
import me.neversleeps.repository.gremlin.mappers.toProject
import org.apache.tinkerpop.gremlin.driver.Cluster
import org.apache.tinkerpop.gremlin.driver.exception.ResponseException
import org.apache.tinkerpop.gremlin.driver.remote.DriverRemoteConnection
import org.apache.tinkerpop.gremlin.process.traversal.AnonymousTraversalSource.traversal
import org.apache.tinkerpop.gremlin.process.traversal.TextP
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversalSource
import org.apache.tinkerpop.gremlin.structure.Vertex
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.`__` as gr

class ProjectRepositoryGremlin(
    private val hosts: String,
    private val port: Int = 8182,
    private val enableSsl: Boolean = false,
    private val user: String = "root",
    private val pass: String = "",
    initObjects: List<Project> = emptyList(),
    initRepository: ((GraphTraversalSource) -> Unit)? = null,
    val randomUuid: () -> String = { uuid4().toString() },
) : IProjectRepository {

    val initializedObjects: List<Project>

    private val cluster by lazy {
        Cluster.build().apply {
            addContactPoints(hosts)
            port(port)
            credentials(user, pass)
            enableSsl(enableSsl)
        }.create()
    }
    private val g by lazy { traversal().withRemote(DriverRemoteConnection.using(cluster)) }

    init {
        if (initRepository != null) {
            initRepository(g)
        }
        initializedObjects = initObjects.map { save(it) }
    }

    private fun save(project: Project): Project = g.addV(project.label())
        .addProject(project)
        .projectList()
        .next()
        ?.toProject()
        ?: throw RuntimeException("Cannot initialize object $project")

    override suspend fun createProject(request: DbProjectRequest): DbProjectResponse {
        val key = randomUuid()
        val project = request.project.copy(id = ProjectId(key), lock = AppLock(randomUuid()))
        val dbRes = try {
            g.addV(project.label())
                .addProject(project)
                .projectList()
                .toList()
        } catch (e: Throwable) {
            if (e is ResponseException || e.cause is ResponseException) {
                return resultErrorNotFound(key)
            }
            return DbProjectResponse(
                data = null,
                isSuccess = false,
                errors = listOf(e.asAppError()),
            )
        }
        return when (dbRes.size) {
            0 -> resultErrorNotFound(key)
            1 -> DbProjectResponse(
                data = dbRes.first().toProject(),
                isSuccess = true,
            )

            else -> errorDuplication(key)
        }
    }

    override suspend fun readProject(request: DbProjectIdRequest): DbProjectResponse {
        val key = request.id.takeIf { it != ProjectId.NONE }?.asString() ?: return resultErrorEmptyId
        val dbRes = try {
            g.V(key).projectList().toList()
        } catch (e: Throwable) {
            if (e is ResponseException || e.cause is ResponseException) {
                return resultErrorNotFound(key)
            }
            return DbProjectResponse(
                data = null,
                isSuccess = false,
                errors = listOf(e.asAppError()),
            )
        }
        return when (dbRes.size) {
            0 -> resultErrorNotFound(key)
            1 -> DbProjectResponse(
                data = dbRes.first().toProject(),
                isSuccess = true,
            )

            else -> errorDuplication(key)
        }
    }

    override suspend fun updateProject(request: DbProjectRequest): DbProjectResponse {
        val key = request.project.id.takeIf { it != ProjectId.NONE }?.asString() ?: return resultErrorEmptyId
        val oldLock = request.project.lock.takeIf { it != AppLock.NONE } ?: return resultErrorEmptyLock
        val newLock = AppLock(randomUuid())
        val newProject = request.project.copy(lock = newLock)
        val dbRes = try {
            g
                .V(key)
                .`as`("a")
                .choose(
                    gr.select<Vertex, Any>("a")
                        .values<String>(FIELD_LOCK)
                        .`is`(oldLock.asString()),
                    gr.select<Vertex, Vertex>("a").addProject(newProject).projectList(),
                    gr.select<Vertex, Vertex>("a").projectList(result = RESULT_LOCK_FAILURE),
                )
                .toList()
        } catch (e: Throwable) {
            if (e is ResponseException || e.cause is ResponseException) {
                return resultErrorNotFound(key)
            }
            return DbProjectResponse(
                data = null,
                isSuccess = false,
                errors = listOf(e.asAppError()),
            )
        }
        val projectResult = dbRes.firstOrNull()?.toProject()
        return when {
            projectResult == null -> resultErrorNotFound(key)
            dbRes.size > 1 -> errorDuplication(key)
            projectResult.lock != newLock -> DbProjectResponse(
                data = projectResult,
                isSuccess = false,
                errors = listOf(
                    errorRepositoryConcurrency(
                        expectedLock = oldLock,
                        actualLock = projectResult.lock,
                    ),
                ),
            )

            else -> DbProjectResponse(
                data = projectResult,
                isSuccess = true,
            )
        }
    }

    override suspend fun deleteProject(request: DbProjectIdRequest): DbProjectResponse {
        val key = request.id.takeIf { it != ProjectId.NONE }?.asString() ?: return resultErrorEmptyId
        val oldLock = request.lock.takeIf { it != AppLock.NONE } ?: return resultErrorEmptyLock
        val dbRes = try {
            g
                .V(key)
                .`as`("a")
                .choose(
                    gr.select<Vertex, Vertex>("a")
                        .values<String>(FIELD_LOCK)
                        .`is`(oldLock.asString()),
                    gr.select<Vertex, Vertex>("a")
                        .sideEffect(gr.drop<Vertex>())
                        .projectList(),
                    gr.select<Vertex, Vertex>("a")
                        .projectList(result = RESULT_LOCK_FAILURE),
                )
                .toList()
        } catch (e: Throwable) {
            if (e is ResponseException || e.cause is ResponseException) {
                return resultErrorNotFound(key)
            }
            return DbProjectResponse(
                data = null,
                isSuccess = false,
                errors = listOf(e.asAppError()),
            )
        }
        val dbFirst = dbRes.firstOrNull()
        val projectsResult = dbFirst?.toProject()
        return when {
            projectsResult == null -> resultErrorNotFound(key)
            dbRes.size > 1 -> errorDuplication(key)
            dbFirst[FIELD_TMP_RESULT] == RESULT_LOCK_FAILURE -> DbProjectResponse(
                data = projectsResult,
                isSuccess = false,
                errors = listOf(
                    errorRepositoryConcurrency(
                        expectedLock = oldLock,
                        actualLock = projectsResult.lock,
                    ),
                ),
            )

            else -> DbProjectResponse(
                data = projectsResult,
                isSuccess = true,
            )
        }
    }

    /**
     * Поиск проектов по фильтру
     * Если в фильтре не установлен какой-либо из параметров - по нему фильтрация не идет
     */
    override suspend fun searchProjects(request: DbProjectFilterRequest): DbProjectsResponse {
        val result = try {
            g.V()
                .apply { request.createdBy.takeIf { it != UserId.NONE }?.also { has(FIELD_CREATED_BY, it.asString()) } }
                .apply { request.searchText.takeIf { it.isNotBlank() }?.also { has(FIELD_TITLE, TextP.containing(it)) } }
                .projectList()
                .toList()
        } catch (e: Throwable) {
            return DbProjectsResponse(
                isSuccess = false,
                data = null,
                errors = listOf(e.asAppError()),
            )
        }
        return DbProjectsResponse(
            data = result.map { it.toProject() },
            isSuccess = true,
        )
    }

    companion object {
        val resultErrorEmptyId = DbProjectResponse(
            data = null,
            isSuccess = false,
            errors = listOf(
                AppError(
                    field = "id",
                    message = "Id must not be null or blank",
                ),
            ),
        )
        val resultErrorEmptyLock = DbProjectResponse(
            data = null,
            isSuccess = false,
            errors = listOf(
                AppError(
                    field = "lock",
                    message = "Lock must be provided",
                ),
            ),
        )

        fun resultErrorNotFound(key: String, e: Throwable? = null) = DbProjectResponse(
            isSuccess = false,
            data = null,
            errors = listOf(
                AppError(
                    code = "not-found",
                    field = "id",
                    message = "Not Found object with key $key",
                    exception = e,
                ),
            ),
        )

        fun errorDuplication(key: String) = DbProjectResponse(
            data = null,
            isSuccess = false,
            errors = listOf(
                errorAdministration(
                    violationCode = "duplicateObjects",
                    description = "Database consistency failure",
                    exception = DuplicatedDbElementsException("Db contains multiple elements for id = '$key'"),
                ),
            ),
        )
    }
}
