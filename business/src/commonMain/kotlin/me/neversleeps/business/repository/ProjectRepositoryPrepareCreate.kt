package me.neversleeps.business.repository

import me.neversleeps.common.ProjectContext
import me.neversleeps.common.models.AppState
import me.neversleeps.lib.cor.ICorChainDsl
import me.neversleeps.lib.cor.worker

fun ICorChainDsl<ProjectContext>.repositoryPrepareCreate(title: String) = worker {
    this.title = title
    description = "Подготовка проекта к сохранению в базе данных"
    on { state == AppState.RUNNING }
    handle {
        projectRepositoryRead = projectValidated.deepCopy()
        projectRepositoryRead.createdBy = principal.id
        projectRepositoryPrepare = projectRepositoryRead
    }
}
