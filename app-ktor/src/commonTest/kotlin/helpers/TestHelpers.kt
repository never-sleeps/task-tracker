package helpers

import me.neversleeps.`in`.memory.project.ProjectRepositoryInMemory
import me.neversleeps.app.AppSettings
import me.neversleeps.app.base.KtorAuthConfig
import me.neversleeps.common.CorSettings
import me.neversleeps.common.repository.project.IProjectRepository
import me.neversleeps.repository.stubs.ProjectRepositoryStub

fun testSettings(repo: IProjectRepository? = null) = AppSettings(
    corSettings = CorSettings(
        repositoryStub = ProjectRepositoryStub(),
        repositoryTest = repo ?: ProjectRepositoryInMemory(),
        repositoryProd = repo ?: ProjectRepositoryInMemory(),
    ),
    auth = KtorAuthConfig.TEST
)
