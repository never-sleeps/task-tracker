package me.neversleeps.app.jackson

import com.fasterxml.jackson.module.kotlin.readValue
import io.ktor.websocket.* // ktlint-disable no-wildcard-imports
import kotlinx.coroutines.channels.ClosedReceiveChannelException
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import me.neversleeps.api.jackson.apiMapper
import me.neversleeps.api.jackson.v1.models.IRequest
import me.neversleeps.app.AppSettings
import me.neversleeps.business.ProjectProcessor
import me.neversleeps.common.ProjectContext
import me.neversleeps.common.helpers.isUpdatableCommand
import me.neversleeps.common.models.AppCommand
import me.neversleeps.mappers.jackson.fromInternal.toTransport
import me.neversleeps.mappers.jackson.fromInternal.toTransportInit
import me.neversleeps.mappers.jackson.fromTransport.fromTransport
import me.neversleeps.mappers.log1.toLog

class WsProjectControllerV1 {
    private val mutex = Mutex()
    private val sessions = mutableSetOf<WebSocketSession>()

    suspend fun handle(session: WebSocketSession, appSettings: AppSettings) {
        mutex.withLock {
            sessions.add(session)
        }

        val logger = appSettings.corSettings.loggerProvider.logger(WsProjectControllerV1::class) // with logging

        val projectContext = ProjectContext()
        val initProject = apiMapper.writeValueAsString(projectContext.toTransportInit())
        session.outgoing.send(Frame.Text(initProject))

        session.incoming.receiveAsFlow().filterIsInstance<Frame.Text>().mapNotNull {
            val frame = it as? Frame.Text ?: return@mapNotNull
            val jsonStr = frame.readText()

            // Handle without flow destruction
            appSettings.projectProcessor.process(
                logger = logger,
                logId = "webSocket",
                command = AppCommand.NONE,
                fromTransport = { ctx ->
                    val request = apiMapper.readValue<IRequest>(jsonStr)
                    ctx.fromTransport(request)
                },
                sendResponse = { ctx ->
                    try {
                        val result = apiMapper.writeValueAsString(ctx.toTransport())
                        // If change request, response is sent to everyone
                        if (ctx.isUpdatableCommand()) {
                            mutex.withLock {
                                sessions.forEach {
                                    it.send(Frame.Text(result))
                                }
                            }
                        } else {
                            session.outgoing.send(Frame.Text(result))
                        }
                    } catch (_: ClosedReceiveChannelException) {
                        mutex.withLock {
                            sessions.clear()
                        }
                    }
                },
                { logId -> toLog(logId) },
            )
        }.collect()
    }
}
