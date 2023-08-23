package me.neversleeps.repository.gremlin

import me.neversleeps.`in`.memory.project.RepositoryReadTest
import me.neversleeps.common.models.project.Project

class RepositoryGremlinReadTest : RepositoryReadTest() {
    override val repo: ProjectRepositoryGremlin by lazy {
        ProjectRepositoryGremlin(
            hosts = ArcadeDbContainer.container.host,
            port = ArcadeDbContainer.container.getMappedPort(8182),
            user = ArcadeDbContainer.username,
            pass = ArcadeDbContainer.password,
            enableSsl = false,
            initObjects = initObjects,
            initRepository = { g -> g.V().drop().iterate() },
        )
    }
    override val readSucc: Project by lazy { repo.initializedObjects[0] }
}
