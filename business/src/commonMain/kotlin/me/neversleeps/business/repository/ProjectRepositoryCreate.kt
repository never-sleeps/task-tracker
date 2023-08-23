package me.neversleeps.business.repository

import me.neversleeps.common.ProjectContext
import me.neversleeps.common.models.AppState
import me.neversleeps.common.repository.project.DbProjectRequest
import me.neversleeps.lib.cor.ICorChainDsl
import me.neversleeps.lib.cor.worker

fun ICorChainDsl<ProjectContext>.repositoryCreate(title: String) = worker {
    this.title = title
    description = "Добавление проекта в БД"
    on { state == AppState.RUNNING }
    handle {
        val request = DbProjectRequest(projectRepositoryPrepare)
        val result = projectRepository.createProject(request)
        val resultProject = result.data
        if (result.isSuccess && resultProject != null) {
            projectRepositoryDone = resultProject
        } else {
            state = AppState.FAILING
            errors.addAll(result.errors)
        }
    }
}
