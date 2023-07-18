package me.neversleeps.common.helpers

import me.neversleeps.common.ProjectContext
import me.neversleeps.common.TaskContext
import me.neversleeps.common.models.AppError
import me.neversleeps.common.models.AppState

fun Throwable.asAppError(
    code: String = "unknown",
    group: String = "exceptions",
    message: String = this.message ?: "",
) = AppError(
    code = code,
    group = group,
    field = "",
    message = message,
    exception = this,
)

fun ProjectContext.addError(vararg error: AppError) = errors.addAll(error)

fun TaskContext.addError(vararg error: AppError) = errors.addAll(error)

fun ProjectContext.fail(error: AppError) {
    addError(error)
    state = AppState.FAILING
}

fun TaskContext.fail(error: AppError) {
    addError(error)
    state = AppState.FAILING
}
