package me.neversleeps.business.repository

import me.neversleeps.common.ProjectContext
import me.neversleeps.common.models.AppState
import me.neversleeps.common.repository.project.DbProjectIdRequest
import me.neversleeps.lib.cor.ICorChainDsl
import me.neversleeps.lib.cor.worker

fun ICorChainDsl<ProjectContext>.repositoryDelete(title: String) = worker {
    this.title = title
    description = "Удаление проекта из БД по ID"
    on { state == AppState.RUNNING }
    handle {
        val request = DbProjectIdRequest(projectRepositoryPrepare)
        val result = projectRepository.deleteProject(request)
        if (!result.isSuccess) {
            state = AppState.FAILING
            errors.addAll(result.errors)
        }
        projectRepositoryDone = projectRepositoryRead
    }
}
