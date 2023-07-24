package me.neversleeps.appspring.v2.ws

import kotlinx.coroutines.runBlocking
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import me.neversleeps.api.multiplatform.apiMapper
import me.neversleeps.api.multiplatform.v1.models.IRequest
import me.neversleeps.business.ProjectProcessor
import me.neversleeps.common.CorSettings
import me.neversleeps.common.ProjectContext
import me.neversleeps.common.helpers.isUpdatableCommand
import me.neversleeps.common.models.AppCommand
import me.neversleeps.mappers.multiplatform.fromInternal.toTransport
import me.neversleeps.mappers.multiplatform.fromInternal.toTransportInit
import me.neversleeps.mappers.multiplatform.fromTransport.fromTransport
import org.springframework.stereotype.Component
import org.springframework.web.socket.CloseStatus
import org.springframework.web.socket.TextMessage
import org.springframework.web.socket.WebSocketSession
import org.springframework.web.socket.handler.TextWebSocketHandler

@Component
class WsProjectHandlerV2(
    private val processor: ProjectProcessor,
    settings: CorSettings,
) : TextWebSocketHandler() {
    private val logger = settings.loggerProvider.logger(WsProjectHandlerV2::class)
    private val sessions = mutableMapOf<String, WebSocketSession>()

    override fun afterConnectionEstablished(session: WebSocketSession) {
        sessions[session.id] = session

        val context = ProjectContext()

        val msg = apiMapper.encodeToString(context.toTransportInit())
        session.sendMessage(TextMessage(msg))
    }

    override fun handleTextMessage(session: WebSocketSession, message: TextMessage) {
        runBlocking {
            processor.process(
                logger,
                "ws-v2",
                AppCommand.NONE,
                { ctx ->
                    val request = apiMapper.decodeFromString<IRequest>(message.payload)
                    ctx.fromTransport(request)
                },
                { ctx ->
                    val result = apiMapper.encodeToString(ctx.toTransport())
                    if (ctx.isUpdatableCommand()) {
                        sessions.values.forEach {
                            it.sendMessage(TextMessage(result))
                        }
                    } else {
                        session.sendMessage(TextMessage(result))
                    }
                },
            )
        }
    }

    override fun afterConnectionClosed(session: WebSocketSession, status: CloseStatus) {
        sessions.remove(session.id)
    }
}
