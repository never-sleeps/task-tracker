package me.neversleeps.appspring.v2.controller

import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import me.neversleeps.api.multiplatform.apiMapper
import me.neversleeps.api.multiplatform.v1.models.IRequest
import me.neversleeps.api.multiplatform.v1.models.IResponse
import me.neversleeps.business.ProjectProcessor
import me.neversleeps.common.models.AppCommand
import me.neversleeps.logging.common.ILogWrapper
import me.neversleeps.mappers.multiplatform.fromInternal.toTransport
import me.neversleeps.mappers.multiplatform.fromTransport.fromTransport

suspend inline fun <
    reified Q : IRequest,
    @Suppress("unused")
    reified R : IResponse,
    > processV2(
    processor: ProjectProcessor,
    command: AppCommand,
    requestString: String,
    logger: ILogWrapper,
    logId: String,
): String = processor.process(
    logger,
    logId,
    command,
    { ctx ->
        val request = apiMapper.decodeFromString<Q>(requestString)
        ctx.fromTransport(request)
    },
    { ctx -> apiMapper.encodeToString(ctx.toTransport()) },
)
