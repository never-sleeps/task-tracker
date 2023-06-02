package ru.otus.otuskotlin.marketplace.common

import kotlinx.datetime.Instant
import ru.otus.otuskotlin.marketplace.common.models.AppCommand
import ru.otus.otuskotlin.marketplace.common.models.AppError
import ru.otus.otuskotlin.marketplace.common.models.AppState
import ru.otus.otuskotlin.marketplace.common.models.RequestId

interface IContext {
    var requestId: RequestId
    var timeStart: Instant
    var command: AppCommand
    var state: AppState

    var errors: MutableList<AppError>
}
