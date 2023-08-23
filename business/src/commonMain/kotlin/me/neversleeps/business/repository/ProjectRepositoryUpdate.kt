package me.neversleeps.business.repository

import me.neversleeps.common.ProjectContext
import me.neversleeps.common.models.AppState
import me.neversleeps.common.repository.project.DbProjectRequest
import me.neversleeps.lib.cor.ICorChainDsl
import me.neversleeps.lib.cor.worker

fun ICorChainDsl<ProjectContext>.repositoryUpdate(title: String) = worker {
    this.title = title
    on { state == AppState.RUNNING }
    handle {
        val request = DbProjectRequest(projectRepositoryPrepare)
        val result = projectRepository.updateProject(request)
        val resultAd = result.data
        if (result.isSuccess && resultAd != null) {
            projectRepositoryDone = resultAd
        } else {
            state = AppState.FAILING
            errors.addAll(result.errors)
            projectRepositoryDone
        }
    }
}
