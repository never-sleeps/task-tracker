package me.neversleeps.common

import kotlinx.datetime.Instant
import me.neversleeps.common.models.AppCommand
import me.neversleeps.common.models.AppError
import me.neversleeps.common.models.AppState
import me.neversleeps.common.models.RequestId

interface IContext {
    var requestId: RequestId
    var timeStart: Instant
    var command: AppCommand
    var state: AppState

    var errors: MutableList<AppError>
}
