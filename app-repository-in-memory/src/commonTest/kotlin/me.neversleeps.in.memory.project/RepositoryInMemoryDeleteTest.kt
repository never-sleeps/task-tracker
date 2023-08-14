package me.neversleeps.`in`.memory.project // ktlint-disable package-name

import me.neversleeps.common.repository.project.IProjectRepository

class RepositoryInMemoryDeleteTest : RepositoryDeleteTest() {
    override val repo: IProjectRepository = ProjectRepositoryInMemory(
        initObjects = initObjects,
    )
}
