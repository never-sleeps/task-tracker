package me.neversleeps.business.groups

import me.neversleeps.common.ProjectContext
import me.neversleeps.common.TaskContext
import me.neversleeps.common.models.AppCommand
import me.neversleeps.common.models.AppState
import me.neversleeps.lib.cor.ICorChainDsl
import me.neversleeps.lib.cor.chain

fun ICorChainDsl<ProjectContext>.projectOperation(
    title: String,
    command: AppCommand,
    block: ICorChainDsl<ProjectContext>.() -> Unit,
) =
    chain {
        block()
        this.title = title
        on { this.command == command && state == AppState.RUNNING }
    }

fun ICorChainDsl<TaskContext>.taskOperation(
    title: String,
    command: AppCommand,
    block: ICorChainDsl<TaskContext>.() -> Unit,
) =
    chain {
        block()
        this.title = title
        on { this.command == command && state == AppState.RUNNING }
    }
