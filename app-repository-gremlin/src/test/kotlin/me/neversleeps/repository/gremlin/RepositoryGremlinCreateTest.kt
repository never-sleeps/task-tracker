package me.neversleeps.repository.gremlin

import me.neversleeps.`in`.memory.project.RepositoryCreateTest
import me.neversleeps.`in`.memory.project.RepositorySearchTest
import me.neversleeps.common.repository.project.IProjectRepository

class RepositoryGremlinCreateTest : RepositoryCreateTest() {
    override val repo: IProjectRepository by lazy {
        ProjectRepositoryGremlin(
            hosts = ArcadeDbContainer.container.host,
            port = ArcadeDbContainer.container.getMappedPort(8182),
            enableSsl = false,
            user = ArcadeDbContainer.username,
            pass = ArcadeDbContainer.password,
            initObjects = RepositorySearchTest.initObjects,
            initRepository = { g -> g.V().drop().iterate() },
            randomUuid = { lockNew.asString() },
        )
    }
}
