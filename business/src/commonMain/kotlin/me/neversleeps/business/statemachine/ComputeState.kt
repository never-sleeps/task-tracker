package me.neversleeps.business.statemachine

import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import me.neversleeps.common.NONE
import me.neversleeps.common.ProjectContext
import me.neversleeps.common.models.AppState
import me.neversleeps.common.statemachine.ObjectState
import me.neversleeps.lib.cor.ICorChainDsl
import me.neversleeps.lib.cor.worker
import kotlin.reflect.KClass

private val machine = SMStateResolver()
private val clazz: KClass<*> = ICorChainDsl<ProjectContext>::computeState::class

fun ICorChainDsl<ProjectContext>.computeState(title: String) = worker {
    this.title = title
    this.description = "Вычисление состояния проекта"
    on { state == AppState.RUNNING }
    handle {
        val log = settings.loggerProvider.logger(clazz)
        val timeNow = Clock.System.now()
        val project = projectValidated

        val prevState = project.objectState
        val timePublished = project.timePublished.takeIf { it != Instant.NONE } ?: timeNow
        val signal = SMSignal(
            state = prevState.takeIf { it != ObjectState.NONE } ?: ObjectState.NEW,
            duration = timeNow - timePublished,
        )
        val transition = machine.resolve(signal)
        if (transition.state != prevState) {
            log.info("New project state transition: ${transition.description}")
        }
        project.objectState = transition.state
    }
}
