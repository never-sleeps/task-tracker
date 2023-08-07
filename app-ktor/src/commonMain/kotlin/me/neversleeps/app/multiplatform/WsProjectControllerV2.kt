package me.neversleeps.app.multiplatform

import io.ktor.websocket.* // ktlint-disable no-wildcard-imports
import kotlinx.coroutines.channels.ClosedReceiveChannelException
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import me.neversleeps.api.multiplatform.apiMapper
import me.neversleeps.api.multiplatform.apiResponseSerialize
import me.neversleeps.api.multiplatform.v1.models.IRequest
import me.neversleeps.app.AppSettings
import me.neversleeps.business.ProjectProcessor
import me.neversleeps.common.ProjectContext
import me.neversleeps.common.helpers.isUpdatableCommand
import me.neversleeps.common.models.AppCommand
import me.neversleeps.mappers.log1.toLog
import me.neversleeps.mappers.multiplatform.fromInternal.toTransport
import me.neversleeps.mappers.multiplatform.fromInternal.toTransportInit
import me.neversleeps.mappers.multiplatform.fromTransport.fromTransport

class WsProjectControllerV2 {
    private val mutex = Mutex()
    private val sessions = mutableSetOf<WebSocketSession>()

    suspend fun handle(session: WebSocketSession, appSettings: AppSettings) {
        mutex.withLock { sessions.add(session) }

        val projectContext = ProjectContext()
        val initProject = apiMapper.encodeToString(projectContext.toTransportInit())
        session.outgoing.send(Frame.Text(initProject))

        val logger = appSettings.corSettings.loggerProvider.logger(WsProjectControllerV2::class)

        session.incoming.receiveAsFlow().mapNotNull { it ->
            val frame = it as? Frame.Text ?: return@mapNotNull

            val jsonStr = frame.readText()

            // Handle without flow destruction
            appSettings.projectProcessor.process(
                logger = logger,
                logId = "webSocket",
                command = AppCommand.NONE,
                fromTransport = { ctx ->
                    val request = apiMapper.decodeFromString<IRequest>(jsonStr)
                    ctx.fromTransport(request)
                },
                sendResponse = { ctx ->
                    try {
                        val result = apiResponseSerialize(ctx.toTransport())

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
