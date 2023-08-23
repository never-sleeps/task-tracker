package me.neversleeps.repository.cassandra

import me.neversleeps.common.models.AppLock
import me.neversleeps.common.models.project.Project
import me.neversleeps.common.repository.project.IProjectRepository
import me.neversleeps.`in`.memory.project.RepositoryCreateTest
import me.neversleeps.`in`.memory.project.RepositoryDeleteTest
import me.neversleeps.`in`.memory.project.RepositoryReadTest
import me.neversleeps.`in`.memory.project.RepositorySearchTest
import me.neversleeps.`in`.memory.project.RepositoryUpdateTest
import org.testcontainers.containers.CassandraContainer
import java.time.Duration

class RepositoryProjectCassandraCreateTest : RepositoryCreateTest() {
    override val repo: IProjectRepository = TestCompanion.repository(initObjects, "ks_create", lockNew)
}

class RepositoryProjectCassandraDeleteTest : RepositoryDeleteTest() {
    override val repo: IProjectRepository = TestCompanion.repository(initObjects, "ks_delete", lockOld)
}

class RepositoryProjectCassandraReadTest : RepositoryReadTest() {
    override val repo: IProjectRepository = TestCompanion.repository(initObjects, "ks_read", AppLock(""))
}

class RepositoryProjectCassandraSearchTest : RepositorySearchTest() {
    override val repo: IProjectRepository = TestCompanion.repository(initObjects, "ks_search", AppLock(""))
}

class RepositoryProjectCassandraUpdateTest : RepositoryUpdateTest() {
    override val repo: IProjectRepository = TestCompanion.repository(initObjects, "ks_update", lockNew)
}

class TestCasandraContainer : CassandraContainer<TestCasandraContainer>("cassandra:3.11.2")

object TestCompanion {
    private val container by lazy {
        TestCasandraContainer().withStartupTimeout(Duration.ofSeconds(300L))
            .also { it.start() }
    }

    fun repository(initObjects: List<Project>, keyspace: String, lock: AppLock): RepositoryProjectCassandra {
        return RepositoryProjectCassandra(
            keyspaceName = keyspace,
            host = container.host,
            port = container.getMappedPort(CassandraContainer.CQL_PORT),
            testing = true,
            randomUuid = { lock.asString() },
            initObjects = initObjects,
        )
    }
}
