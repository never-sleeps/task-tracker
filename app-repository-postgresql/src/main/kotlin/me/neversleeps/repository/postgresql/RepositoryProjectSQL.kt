package me.neversleeps.repository.postgresql

import com.benasher44.uuid.uuid4
import me.neversleeps.common.helpers.asAppError
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
import org.jetbrains.exposed.sql.* // ktlint-disable no-wildcard-imports
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.transactions.transaction

class RepositoryProjectSQL(
    properties: SqlProperties,
    initObjects: Collection<Project> = emptyList(),
    val randomUuid: () -> String = { uuid4().toString() },
) : IProjectRepository {

    init {
        val driver = when {
            properties.url.startsWith("jdbc:postgresql://") -> "org.postgresql.Driver"
            else -> throw IllegalArgumentException("Unknown driver for url ${properties.url}")
        }

        Database.connect(
            properties.url,
            driver,
            properties.user,
            properties.password,
        )

        transaction {
            if (properties.dropDatabase) SchemaUtils.drop(ProjectTable)
            SchemaUtils.create(ProjectTable)
            initObjects.forEach { createProject(it) }
        }
    }

    private fun createProject(project: Project): Project {
        val res = ProjectTable.insert {
            to(it, project, randomUuid)
        }

        return ProjectTable.from(res)
    }

    private fun <T> transactionWrapper(block: () -> T, handle: (Exception) -> T): T =
        try {
            transaction {
                block()
            }
        } catch (e: Exception) {
            handle(e)
        }

    private fun transactionWrapper(block: () -> DbProjectResponse): DbProjectResponse =
        transactionWrapper(block) { DbProjectResponse.error(it.asAppError()) }

    override suspend fun createProject(request: DbProjectRequest): DbProjectResponse = transactionWrapper {
        DbProjectResponse.success(createProject(request.project))
    }

    private fun read(id: ProjectId): DbProjectResponse {
        val res = ProjectTable.select {
            ProjectTable.id eq id.asString()
        }.singleOrNull() ?: return DbProjectResponse.errorNotFound
        return DbProjectResponse.success(ProjectTable.from(res))
    }

    override suspend fun readProject(request: DbProjectIdRequest): DbProjectResponse =
        transactionWrapper { read(request.id) }

    private fun update(
        id: ProjectId,
        lock: AppLock,
        block: (Project) -> DbProjectResponse,
    ): DbProjectResponse =
        transactionWrapper {
            if (id == ProjectId.NONE) return@transactionWrapper DbProjectResponse.errorEmptyId

            val current = ProjectTable.select { ProjectTable.id eq id.asString() }
                .firstOrNull()
                ?.let { ProjectTable.from(it) }

            when {
                current == null -> DbProjectResponse.errorNotFound
                current.lock != lock -> DbProjectResponse.errorConcurrent(lock, current)
                else -> block(current)
            }
        }

    override suspend fun updateProject(request: DbProjectRequest): DbProjectResponse =
        update(request.project.id, request.project.lock) {
            ProjectTable.update({ ProjectTable.id eq request.project.id.asString() }) {
                to(it, request.project, randomUuid)
            }
            read(request.project.id)
        }

    override suspend fun deleteProject(request: DbProjectIdRequest): DbProjectResponse =
        update(request.id, request.lock) {
            ProjectTable.deleteWhere { ProjectTable.id eq request.id.asString() }
            DbProjectResponse.success(it)
        }

    override suspend fun searchProjects(request: DbProjectFilterRequest): DbProjectsResponse =
        transactionWrapper({
            val res = ProjectTable.select {
                buildList {
                    add(Op.TRUE)
                    if (request.createdBy != UserId.NONE) {
                        add(ProjectTable.createdBy eq request.createdBy.asString())
                    }
                    if (request.searchText.isNotBlank()) {
                        add(
                            (ProjectTable.title like "%${request.searchText}%")
                                or (ProjectTable.description like "%${request.searchText}%"),
                        )
                    }
                }.reduce { a, b -> a and b }
            }
            DbProjectsResponse(data = res.map { ProjectTable.from(it) }, isSuccess = true)
        }, {
            DbProjectsResponse.error(it.asAppError())
        })
}
