package me.neversleeps.repository.gremlin

import me.neversleeps.`in`.memory.project.RepositoryUpdateTest
import me.neversleeps.common.models.project.Project

class RepositoryGremlinUpdateTest : RepositoryUpdateTest() {
    override val repo: ProjectRepositoryGremlin by lazy {
        ProjectRepositoryGremlin(
            hosts = ArcadeDbContainer.container.host,
            port = ArcadeDbContainer.container.getMappedPort(8182),
            enableSsl = false,
            user = ArcadeDbContainer.username,
            pass = ArcadeDbContainer.password,
            initObjects = initObjects,
            initRepository = { g -> g.V().drop().iterate() },
            randomUuid = { lockNew.asString() },
        )
    }
    override val updateSuccess: Project by lazy { repo.initializedObjects[0] }
    override val updateConcurrent: Project by lazy { repo.initializedObjects[1] }
}
