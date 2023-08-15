package me.neversleeps.repository.postgresql

import me.neversleeps.common.repository.project.IProjectRepository
import me.neversleeps.`in`.memory.project.RepositoryCreateTest
import me.neversleeps.`in`.memory.project.RepositoryDeleteTest
import me.neversleeps.`in`.memory.project.RepositoryReadTest
import me.neversleeps.`in`.memory.project.RepositorySearchTest
import me.neversleeps.`in`.memory.project.RepositoryUpdateTest

class RepositoryProjectSQLCreateTest : RepositoryCreateTest() {
    override val repo: IProjectRepository = SqlTestCompanion.repositoryUnderTestContainer(
        initObjects,
        randomUuid = { lockNew.asString() },
    )
}

class RepoAdSQLDeleteTest : RepositoryDeleteTest() {
    override val repo: IProjectRepository = SqlTestCompanion.repositoryUnderTestContainer(initObjects)
}

class RepoAdSQLReadTest : RepositoryReadTest() {
    override val repo: IProjectRepository = SqlTestCompanion.repositoryUnderTestContainer(initObjects)
}

class RepoAdSQLSearchTest : RepositorySearchTest() {
    override val repo: IProjectRepository = SqlTestCompanion.repositoryUnderTestContainer(initObjects)
}

class RepoAdSQLUpdateTest : RepositoryUpdateTest() {
    override val repo: IProjectRepository = SqlTestCompanion.repositoryUnderTestContainer(
        initObjects,
        randomUuid = { lockNew.asString() },
    )
}
