package ru.otus.otuskotlin.marketplace.common.helpers

import ru.otus.otuskotlin.marketplace.common.ProjectContext
import ru.otus.otuskotlin.marketplace.common.models.AppError

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
