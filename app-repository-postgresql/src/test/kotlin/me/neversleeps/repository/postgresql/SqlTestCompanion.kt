package me.neversleeps.repository.postgresql

import com.benasher44.uuid.uuid4
import me.neversleeps.common.models.project.Project
import org.testcontainers.containers.PostgreSQLContainer
import java.time.Duration

class PostgresContainer : PostgreSQLContainer<PostgresContainer>("postgres:13.2")

object SqlTestCompanion {
    private const val USER = "postgres"
    private const val PASS = "tracker-pass"
    private const val SCHEMA = "tracker"

    private val container by lazy {
        PostgresContainer().apply {
            withUsername(USER)
            withPassword(PASS)
            withDatabaseName(SCHEMA)
            withStartupTimeout(Duration.ofSeconds(300L))
            start()
        }
    }

    private val url: String by lazy { container.jdbcUrl }

    fun repositoryUnderTestContainer(
        initObjects: Collection<Project> = emptyList(),
        randomUuid: () -> String = { uuid4().toString() },
    ): RepositoryProjectSQL {
        return RepositoryProjectSQL(
            SqlProperties(url, USER, PASS, SCHEMA, dropDatabase = true),
            initObjects,
            randomUuid = randomUuid,
        )
    }
}
