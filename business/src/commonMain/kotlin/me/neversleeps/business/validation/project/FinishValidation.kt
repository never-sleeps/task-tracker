package me.neversleeps.business.validation.project

import me.neversleeps.common.ProjectContext
import me.neversleeps.common.models.AppState
import me.neversleeps.lib.cor.ICorChainDsl
import me.neversleeps.lib.cor.worker

fun ICorChainDsl<ProjectContext>.finishAdValidation(title: String) = worker {
    this.title = title
    on { state == AppState.RUNNING }
    handle {
        projectValidated = projectValidating
    }
}

fun ICorChainDsl<ProjectContext>.finishAdFilterValidation(title: String) = worker {
    this.title = title
    on { state == AppState.RUNNING }
    handle {
        projectSearchFilterValidated = projectSearchFilterValidating
    }
}
