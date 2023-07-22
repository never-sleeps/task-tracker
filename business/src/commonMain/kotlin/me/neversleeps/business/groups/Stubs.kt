package me.neversleeps.business.groups

import me.neversleeps.common.ProjectContext
import me.neversleeps.common.TaskContext
import me.neversleeps.common.models.AppState
import me.neversleeps.common.stubs.ProjectDebugStub
import me.neversleeps.common.stubs.TaskDebugStub
import me.neversleeps.lib.cor.ICorChainDsl
import me.neversleeps.lib.cor.chain

fun ICorChainDsl<ProjectContext>.projectStubs(title: String, block: ICorChainDsl<ProjectContext>.() -> Unit) =
    chain {
        block()
        this.title = title
        on { stubCase != ProjectDebugStub.NONE && state == AppState.RUNNING }
    }

fun ICorChainDsl<TaskContext>.taskStubs(title: String, block: ICorChainDsl<TaskContext>.() -> Unit) =
    chain {
        block()
        this.title = title
        on { stubCase != TaskDebugStub.NONE && (state == AppState.RUNNING) }
    }
