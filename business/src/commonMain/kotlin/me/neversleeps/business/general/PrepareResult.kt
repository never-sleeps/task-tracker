package me.neversleeps.business.general

import me.neversleeps.common.ProjectContext
import me.neversleeps.common.models.AppState
import me.neversleeps.common.stubs.WorkMode
import me.neversleeps.lib.cor.ICorChainDsl
import me.neversleeps.lib.cor.worker

fun ICorChainDsl<ProjectContext>.prepareResult(title: String) = worker {
    this.title = title
    description = "Подготовка данных для ответа клиенту на запрос"
    on { workMode != WorkMode.STUB }
    handle {
        projectResponse = projectRepositoryDone
        projectsResponse = projectsRepositoryDone
        state = when (val st = state) {
            AppState.RUNNING -> AppState.FINISHING
            else -> st
        }
    }
}
