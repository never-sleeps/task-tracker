package me.neversleeps.`in`.memory.project // ktlint-disable package-name

class RepositoryInMemoryCreateTest : RepositoryCreateTest() {
    override val repo = ProjectRepositoryInMemory(
        initObjects = initObjects,
    )
}
