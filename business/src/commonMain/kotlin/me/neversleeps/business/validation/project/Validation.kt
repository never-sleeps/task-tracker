package me.neversleeps.business.validation.project

import me.neversleeps.common.ProjectContext
import me.neversleeps.common.models.AppState
import me.neversleeps.lib.cor.ICorChainDsl
import me.neversleeps.lib.cor.chain

fun ICorChainDsl<ProjectContext>.projectValidation(block: ICorChainDsl<ProjectContext>.() -> Unit) = chain {
    block()
    title = "Валидация"
    on { state == AppState.RUNNING }
}
