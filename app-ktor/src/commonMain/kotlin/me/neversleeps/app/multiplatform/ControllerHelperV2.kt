package me.neversleeps.app.multiplatform

import io.ktor.server.application.* // ktlint-disable no-wildcard-imports
import io.ktor.server.request.*
import io.ktor.server.response.*
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import me.neversleeps.api.multiplatform.apiMapper
import me.neversleeps.api.multiplatform.v1.models.IRequest
import me.neversleeps.api.multiplatform.v1.models.IResponse
import me.neversleeps.app.AppSettings
import me.neversleeps.common.models.AppCommand
import me.neversleeps.common.models.user.UserId
import me.neversleeps.common.permissions.AppPrincipalModel
import me.neversleeps.common.permissions.AppUserGroups
import me.neversleeps.logging.common.ILogWrapper
import me.neversleeps.mappers.log1.toLog
import me.neversleeps.mappers.multiplatform.fromInternal.toTransport
import me.neversleeps.mappers.multiplatform.fromTransport.fromTransport

suspend inline fun <
    reified Q : IRequest,
    @Suppress("unused")
    reified R : IResponse,
    > ApplicationCall.processV2(

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
            val request = apiMapper.decodeFromString<Q>(receiveText())
            ctx.principal = principal(appSettings)
            ctx.fromTransport(request)
        },
        sendResponse = { ctx ->
            respond(apiMapper.encodeToString(ctx.toTransport()))
        },
        toLog = { logId -> toLog(logId) },
    )
}

// TODO: костыль для решения проблемы отсутствия jwt в native
@Suppress("UnusedReceiverParameter", "UNUSED_PARAMETER")
fun ApplicationCall.principal(appSettings: AppSettings): AppPrincipalModel = AppPrincipalModel(
    id = UserId("some-author"),
    fname = "Ktor-user-fname",
    mname = "Ktor-user-mname",
    lname = "Ktor-user-lname",
    groups = setOf(AppUserGroups.TEST, AppUserGroups.USER),
)
