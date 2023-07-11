package me.neversleeps.appspring.v1.ws

import kotlinx.coroutines.runBlocking
import kotlinx.datetime.Clock
import me.neversleeps.api.jackson.apiMapper
import me.neversleeps.api.jackson.v1.models.IRequest
import me.neversleeps.appspring.service.ProjectBlockingProcessor
import me.neversleeps.common.ProjectContext
import me.neversleeps.common.helpers.asAppError
import me.neversleeps.common.helpers.isUpdatableCommand
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
    private val processor: ProjectBlockingProcessor
) : TextWebSocketHandler() {
    private val sessions = mutableMapOf<String, WebSocketSession>()

    companion object : KLogging()

    override fun afterConnectionEstablished(session: WebSocketSession) {
        sessions[session.id] = session

        val context = ProjectContext()

        val msg = apiMapper.writeValueAsString(context.toTransportInit())
        session.sendMessage(TextMessage(msg))
    }

    override fun handleTextMessage(session: WebSocketSession, message: TextMessage) {
        val ctx = ProjectContext(timeStart = Clock.System.now())

        runBlocking {
            try {
                val request = apiMapper.readValue(message.payload, IRequest::class.java)
                logger.info { "request: $request" }
                ctx.fromTransport(request)

                processor.execute(ctx)

                val result = apiMapper.writeValueAsString(ctx.toTransport())
                logger.info { "result: $result" }
                if (ctx.isUpdatableCommand()) {
                    sessions.values.forEach {
                        it.sendMessage(TextMessage(result))
                    }
                } else {
                    session.sendMessage(TextMessage(result))
                }
            } catch (e: Exception) {
                ctx.errors.add(e.asAppError())

                val response = apiMapper.writeValueAsString(ctx.toTransport())
                session.sendMessage(TextMessage(response))
            }
        }
    }

    override fun afterConnectionClosed(session: WebSocketSession, status: CloseStatus) {
        sessions.remove(session.id)
    }
}
