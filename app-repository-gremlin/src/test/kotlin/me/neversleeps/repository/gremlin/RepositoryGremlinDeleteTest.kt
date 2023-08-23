package me.neversleeps.repository.gremlin

import me.neversleeps.common.models.project.Project
import me.neversleeps.common.models.project.ProjectId
import me.neversleeps.`in`.memory.project.RepositoryDeleteTest

class RepositoryGremlinDeleteTest : RepositoryDeleteTest() {
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
    override val deleteSuccess: Project by lazy { repo.initializedObjects[0] }
    override val deleteConcurrent: Project by lazy { repo.initializedObjects[1] }
    override val notFoundId: ProjectId = ProjectId("#3100:0")
}
