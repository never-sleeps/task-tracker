package me.neversleeps.appspring.v1.ws

import kotlinx.coroutines.runBlocking
import me.neversleeps.api.jackson.apiMapper
import me.neversleeps.api.jackson.v1.models.IRequest
import me.neversleeps.business.ProjectProcessor
import me.neversleeps.common.CorSettings
import me.neversleeps.common.ProjectContext
import me.neversleeps.common.helpers.isUpdatableCommand
import me.neversleeps.common.models.AppCommand
import me.neversleeps.mappers.jackson.fromInternal.toTransport
import me.neversleeps.mappers.jackson.fromInternal.toTransportInit
import me.neversleeps.mappers.jackson.fromTransport.fromTransport
import mu.KLogging
import org.springframework.stereotype.Component
import org.springframework.web.socket.CloseStatus
import org.springframework.web.socket.TextMessage
import org.springframework.web.socket.WebSocketSession
import org.springframework.web.socket.handler.TextWebSocketHandler

@Component
class WsProjectHandlerV1(
    private val processor: ProjectProcessor,
    settings: CorSettings,
) : TextWebSocketHandler() {
    private val logger = settings.loggerProvider.logger(WsProjectHandlerV1::class)
    private val sessions = mutableMapOf<String, WebSocketSession>()

    companion object : KLogging()

    override fun afterConnectionEstablished(session: WebSocketSession) {
        sessions[session.id] = session

        val context = ProjectContext()

        val msg = apiMapper.writeValueAsString(context.toTransportInit())
        session.sendMessage(TextMessage(msg))
    }

    override fun handleTextMessage(session: WebSocketSession, message: TextMessage) {
        runBlocking {
            processor.process(
                logger = logger,
                logId = "ws-v1",
                command = AppCommand.NONE,
                fromTransport = { ctx ->
                    val request = apiMapper.readValue(message.payload, IRequest::class.java)
                    ctx.fromTransport(request)
                },
                sendResponse = { ctx ->
                    val result = apiMapper.writeValueAsString(ctx.toTransport())
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
