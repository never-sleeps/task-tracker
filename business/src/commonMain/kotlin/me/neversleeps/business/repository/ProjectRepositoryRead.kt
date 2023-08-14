package me.neversleeps.business.repository

import me.neversleeps.common.ProjectContext
import me.neversleeps.common.models.AppState
import me.neversleeps.common.repository.project.DbProjectIdRequest
import me.neversleeps.lib.cor.ICorChainDsl
import me.neversleeps.lib.cor.worker

fun ICorChainDsl<ProjectContext>.repositoryRead(title: String) = worker {
    this.title = title
    description = "Чтение проекта из БД"
    on { state == AppState.RUNNING }
    handle {
        val request = DbProjectIdRequest(projectValidated)
        val result = projectRepository.readProject(request)
        val resultAd = result.data
        if (result.isSuccess && resultAd != null) {
            projectRepositoryRead = resultAd
        } else {
            state = AppState.FAILING
            errors.addAll(result.errors)
        }
    }
}
