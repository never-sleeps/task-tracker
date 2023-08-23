package me.neversleeps.business.general

import me.neversleeps.common.ProjectContext
import me.neversleeps.common.helpers.errorAdministration
import me.neversleeps.common.helpers.fail
import me.neversleeps.common.repository.project.IProjectRepository
import me.neversleeps.common.stubs.WorkMode
import me.neversleeps.lib.cor.ICorChainDsl
import me.neversleeps.lib.cor.worker

fun ICorChainDsl<ProjectContext>.initRepo(title: String) = worker {
    this.title = title
    description = """
        Вычисление основного рабочего репозитория в зависимости от запрошенного режима работы        
    """.trimIndent()
    handle {
        projectRepository = when {
            workMode == WorkMode.TEST -> settings.repositoryTest
            workMode == WorkMode.STUB -> settings.repositoryStub
            else -> settings.repositoryProd
        }
        if (workMode != WorkMode.STUB && projectRepository == IProjectRepository.NONE) {
            fail(
                errorAdministration(
                    field = "repo",
                    violationCode = "dbNotConfigured",
                    description = "The database is unconfigured for chosen workmode ($workMode). " +
                        "Please, contact the administrator staff",
                ),
            )
        }
    }
}
