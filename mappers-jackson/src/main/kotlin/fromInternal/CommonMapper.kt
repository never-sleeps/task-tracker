package fromInternal

import me.neversleeps.api.jackson.v1.models.Error
import me.neversleeps.api.jackson.v1.models.ResponseResultStatus
import models.AppError
import models.AppState

fun AppState.toTransport(): ResponseResultStatus =
    if (this == AppState.RUNNING) {
        ResponseResultStatus.SUCCESS
    } else {
        ResponseResultStatus.ERROR
    }

fun List<AppError>.toTransport(): List<Error>? = this
    .map { it.toTransport() }
    .takeIf { it.isNotEmpty() }

fun AppError.toTransport() = Error(
    code = code.takeIf { it.isNotBlank() },
    group = group.takeIf { it.isNotBlank() },
    field = field.takeIf { it.isNotBlank() },
    message = message.takeIf { it.isNotBlank() },
)
