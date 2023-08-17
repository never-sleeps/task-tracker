package me.neversleeps.repository.cassandra

import com.benasher44.uuid.uuid4
import com.datastax.oss.driver.api.core.CqlSession
import com.datastax.oss.driver.api.querybuilder.SchemaBuilder
import com.datastax.oss.driver.internal.core.type.codec.extras.enums.EnumNameCodec
import com.datastax.oss.driver.internal.core.type.codec.registry.DefaultCodecRegistry
import kotlinx.coroutines.future.await
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withTimeout
import me.neversleeps.common.helpers.asAppError
import me.neversleeps.common.models.AppError
import me.neversleeps.common.models.AppLock
import me.neversleeps.common.models.project.Project
import me.neversleeps.common.models.project.ProjectId
import me.neversleeps.common.repository.project.DbProjectFilterRequest
import me.neversleeps.common.repository.project.DbProjectIdRequest
import me.neversleeps.common.repository.project.DbProjectRequest
import me.neversleeps.common.repository.project.DbProjectResponse
import me.neversleeps.common.repository.project.DbProjectsResponse
import me.neversleeps.common.repository.project.IProjectRepository
import me.neversleeps.repository.cassandra.model.ProjectCassandraDTO
import me.neversleeps.repository.cassandra.model.ProjectPermissionEntity
import org.slf4j.LoggerFactory
import java.net.InetAddress
import java.net.InetSocketAddress
import java.util.concurrent.CompletionStage
import kotlin.time.Duration
import kotlin.time.DurationUnit
import kotlin.time.toDuration

class RepositoryProjectCassandra(
    private val keyspaceName: String,
    private val host: String = "",
    private val port: Int = 9042,
    private val user: String = "cassandra",
    private val pass: String = "cassandra",
    private val testing: Boolean = false,
    private val timeout: Duration = 30.toDuration(DurationUnit.SECONDS),
    private val randomUuid: () -> String = { uuid4().toString() },
    initObjects: Collection<Project> = emptyList(),
) : IProjectRepository {
    private val log = LoggerFactory.getLogger(javaClass)

    private val codecRegistry by lazy {
        DefaultCodecRegistry("default").apply {
            register(EnumNameCodec(ProjectPermissionEntity::class.java))
        }
    }

    private val session by lazy {
        CqlSession.builder()
            .addContactPoints(parseAddresses(host, port))
            .withLocalDatacenter("datacenter1")
            .withAuthCredentials(user, pass)
            .withCodecRegistry(codecRegistry)
            .build()
    }

    private val mapper by lazy { CassandraMapper.builder(session).build() }

    private fun createSchema(keyspace: String) {
        session.execute(
            SchemaBuilder
                .createKeyspace(keyspace)
                .ifNotExists()
                .withSimpleStrategy(1)
                .build(),
        )
        session.execute(ProjectCassandraDTO.table(keyspace, ProjectCassandraDTO.TABLE_NAME))
        session.execute(ProjectCassandraDTO.titleIndex(keyspace, ProjectCassandraDTO.TABLE_NAME))
    }

    private val dao by lazy {
        if (testing) {
            createSchema(keyspaceName)
        }
        mapper.projectDao(keyspaceName, ProjectCassandraDTO.TABLE_NAME).apply {
            runBlocking {
                initObjects.map { model ->
                    withTimeout(timeout) {
                        create(ProjectCassandraDTO(model)).await()
                    }
                }
            }
        }
    }

    private fun errorToProjectResponse(e: Exception) = DbProjectResponse.error(e.asAppError())
    private fun errorToProjectsResponse(e: Exception) = DbProjectsResponse.error(e.asAppError())

    private suspend inline fun <DbRes, Response> doDbAction(
        name: String,
        crossinline daoAction: () -> CompletionStage<DbRes>,
        okToResponse: (DbRes) -> Response,
        errorToResponse: (Exception) -> Response,
    ): Response = doDbAction(
        name,
        {
            val dbRes = withTimeout(timeout) { daoAction().await() }
            okToResponse(dbRes)
        },
        errorToResponse,
    )

    private suspend inline fun readAndDoDbAction(
        name: String,
        id: ProjectId,
        successResult: Project?,
        daoAction: () -> CompletionStage<Boolean>,
        errorToResponse: (Exception) -> DbProjectResponse,
    ): DbProjectResponse =
        if (id == ProjectId.NONE) {
            ID_IS_EMPTY
        } else {
            doDbAction(
                name,
                {
                    val read = dao.read(id.asString()).await()
                    if (read == null) {
                        ID_NOT_FOUND
                    } else {
                        val success = daoAction().await()
                        if (success) {
                            DbProjectResponse.success(successResult ?: read.toTransportModel())
                        } else {
                            DbProjectResponse(
                                read.toTransportModel(),
                                false,
                                CONCURRENT_MODIFICATION.errors,
                            )
                        }
                    }
                },
                errorToResponse,
            )
        }

    private inline fun <Response> doDbAction(
        name: String,
        daoAction: () -> Response,
        errorToResponse: (Exception) -> Response,
    ): Response =
        try {
            daoAction()
        } catch (e: Exception) {
            log.error("Failed to $name", e)
            errorToResponse(e)
        }

    override suspend fun createProject(request: DbProjectRequest): DbProjectResponse {
        val new = request.project.copy(id = ProjectId(randomUuid()), lock = AppLock(randomUuid()))
        return doDbAction(
            "create",
            { dao.create(ProjectCassandraDTO(new)) },
            { DbProjectResponse.success(new) },
            ::errorToProjectResponse,
        )
    }

    override suspend fun readProject(request: DbProjectIdRequest): DbProjectResponse =
        if (request.id == ProjectId.NONE) {
            ID_IS_EMPTY
        } else {
            doDbAction(
                "read",
                { dao.read(request.id.asString()) },
                { found ->
                    if (found != null) {
                        DbProjectResponse.success(found.toTransportModel())
                    } else {
                        ID_NOT_FOUND
                    }
                },
                ::errorToProjectResponse,
            )
        }

    override suspend fun updateProject(request: DbProjectRequest): DbProjectResponse {
        val prevLock = request.project.lock.asString()
        val new = request.project.copy(lock = AppLock(randomUuid()))
        val dto = ProjectCassandraDTO(new)

        return readAndDoDbAction(
            "update",
            request.project.id,
            new,
            { dao.update(dto, prevLock) },
            ::errorToProjectResponse,
        )
    }

    override suspend fun deleteProject(request: DbProjectIdRequest): DbProjectResponse =
        readAndDoDbAction(
            "delete",
            request.id,
            null,
            { dao.delete(request.id.asString(), request.lock.asString()) },
            ::errorToProjectResponse,
        )

    override suspend fun searchProjects(request: DbProjectFilterRequest): DbProjectsResponse =
        doDbAction(
            "search",
            { dao.search(request) },
            { found ->
                DbProjectsResponse.success(found.map { it.toTransportModel() })
            },
            ::errorToProjectsResponse,
        )

    companion object {
        private val ID_IS_EMPTY = DbProjectResponse.error(AppError(field = "id", message = "Id is empty"))
        private val ID_NOT_FOUND =
            DbProjectResponse.error(AppError(field = "id", code = "not-found", message = "Not Found"))
        private val CONCURRENT_MODIFICATION =
            DbProjectResponse.error(AppError(field = "lock", code = "concurrency", message = "Concurrent modification"))
    }
}

private fun parseAddresses(hosts: String, port: Int): Collection<InetSocketAddress> = hosts
    .split(Regex("""\s*,\s*"""))
    .map { InetSocketAddress(InetAddress.getByName(it), port) }
