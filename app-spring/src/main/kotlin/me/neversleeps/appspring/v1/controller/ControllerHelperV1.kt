package me.neversleeps.appspring.v1.controller

import me.neversleeps.api.jackson.v1.models.IRequest
import me.neversleeps.api.jackson.v1.models.IResponse
import me.neversleeps.business.ProjectProcessor
import me.neversleeps.common.models.AppCommand
import me.neversleeps.logging.common.ILogWrapper
import me.neversleeps.mappers.jackson.fromInternal.toTransport
import me.neversleeps.mappers.jackson.fromTransport.fromTransport
import me.neversleeps.mappers.log1.toLog

suspend inline fun <reified Q : IRequest, reified R : IResponse> processV1(
    processor: ProjectProcessor,
    command: AppCommand,
    request: Q,
    logger: ILogWrapper,
    logId: String,
): R = processor.process(
    logger,
    logId,
    command,
    { ctx -> ctx.fromTransport(request) },
    { ctx -> ctx.toTransport() as R },
    { logId -> toLog(logId) },
)
