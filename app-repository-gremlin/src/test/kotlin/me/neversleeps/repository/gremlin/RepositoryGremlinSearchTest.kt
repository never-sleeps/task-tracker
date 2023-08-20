package me.neversleeps.repository.gremlin

import me.neversleeps.`in`.memory.project.RepositorySearchTest
import me.neversleeps.common.models.project.Project

class RepositoryGremlinSearchTest : RepositorySearchTest() {
    override val repo: ProjectRepositoryGremlin by lazy {
        ProjectRepositoryGremlin(
            hosts = ArcadeDbContainer.container.host,
            port = ArcadeDbContainer.container.getMappedPort(8182),
            enableSsl = false,
            user = ArcadeDbContainer.username,
            pass = ArcadeDbContainer.password,
            initObjects = initObjects,
            initRepository = { g -> g.V().drop().iterate() },
        )
    }

    override val initializedObjects: List<Project> by lazy {
        repo.initializedObjects
    }
}
