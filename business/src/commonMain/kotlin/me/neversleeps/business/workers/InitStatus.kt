package me.neversleeps.business.workers

import me.neversleeps.common.ProjectContext
import me.neversleeps.common.TaskContext
import me.neversleeps.common.models.AppState
import me.neversleeps.lib.cor.ICorChainDsl
import me.neversleeps.lib.cor.worker

fun ICorChainDsl<ProjectContext>.projectInitStatus(title: String) =
    worker() {
        this.title = title
        on { state == AppState.NONE }
        handle { state = AppState.RUNNING }
    }

fun ICorChainDsl<TaskContext>.taskInitStatus(title: String) =
    worker() {
        this.title = title
        on { state == AppState.NONE }
        handle { state = AppState.RUNNING }
    }
