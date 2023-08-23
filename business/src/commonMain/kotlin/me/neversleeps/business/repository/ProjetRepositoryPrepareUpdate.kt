package me.neversleeps.business.repository

import me.neversleeps.common.ProjectContext
import me.neversleeps.common.models.AppState
import me.neversleeps.lib.cor.ICorChainDsl
import me.neversleeps.lib.cor.worker

fun ICorChainDsl<ProjectContext>.repositoryPrepareUpdate(title: String) = worker {
    this.title = title
    description = "Готовим данные к сохранению в БД: совмещаем данные, прочитанные из БД, " +
        "и данные, полученные от пользователя"
    on { state == AppState.RUNNING }
    handle {
        projectRepositoryPrepare = projectRepositoryRead.deepCopy().apply {
            this.title = projectValidated.title
            description = projectValidated.description
            createdBy = projectValidated.createdBy
            permissions = projectValidated.permissions
        }
    }
}
