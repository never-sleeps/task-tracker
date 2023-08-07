package me.neversleeps.app.jackson

import io.ktor.server.application.* // ktlint-disable no-wildcard-imports
import io.ktor.server.request.*
import io.ktor.server.response.*
import me.neversleeps.api.jackson.v1.models.IRequest
import me.neversleeps.api.jackson.v1.models.IResponse
import me.neversleeps.app.AppSettings
import me.neversleeps.common.models.AppCommand
import me.neversleeps.logging.common.ILogWrapper
import me.neversleeps.mappers.jackson.fromInternal.toTransport
import me.neversleeps.mappers.jackson.fromTransport.fromTransport
import me.neversleeps.mappers.log1.toLog

suspend inline fun <
    reified Q : IRequest,
    @Suppress("unused")
    reified R : IResponse,
    > ApplicationCall.processV1(

    appSettings: AppSettings,
    logger: ILogWrapper,
    logId: String,
    command: AppCommand,
) {
    appSettings.projectProcessor.process(
        logger = logger,
        logId = logId,
        command = command,
        fromTransport = { ctx ->
            val request = receive<Q>()
            ctx.fromTransport(request)
        },
        sendResponse = { ctx ->
            respond(ctx.toTransport())
        },
        { logId -> toLog(logId) },
    )
}
