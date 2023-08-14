package me.neversleeps.`in`.memory.project

import me.neversleeps.common.repository.project.IProjectRepository

class RepositoryInMemoryUpdateTest : RepositoryUpdateTest() {
    override val repo: IProjectRepository = ProjectRepositoryInMemory(
        initObjects = initObjects,
    )
}
