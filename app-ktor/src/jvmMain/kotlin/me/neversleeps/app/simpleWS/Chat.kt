package me.neversleeps.app.simpleWS

import io.ktor.network.selector.* // ktlint-disable no-wildcard-imports
import io.ktor.websocket.*
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.flow.receiveAsFlow

private val sessions = mutableMapOf<WebSocketSession, UserSessionData>()

suspend fun DefaultWebSocketSession.wsChat() {
    send("Please send one of: `name: <name>`, `msg: <message>, `close`")

    incoming.receiveAsFlow().filterIsInstance<Frame.Text>().mapNotNull {
        val text = it.readText()
        try {
            when {
                text.startsWith("name:") -> {
                    val name = text.drop("name:".length).trim()
                    sessions[this] = UserSessionData(name, this)
                }
                text.startsWith("msg:") -> {
                    val current = sessions.getValue(this)
                    sessions.filterKeys { it != this }.values.forEach {
                        val message = text.drop("msg:".length).trim()
                        it.session.send("[${current.name}]: $message")
                    }
                }
                text == "close" -> {
                    val current = sessions.getValue(this)
                    close(CloseReason(CloseReason.Codes.NORMAL, "Client left chat room"))
                    sessions.remove(this)
                    sessions.values.forEach {
                        it.session.send("[${current.name}] left chat")
                    }
                }
                else -> {
                    send("Unknown command")
                }
            }
        } catch (e: ClosedChannelCancellationException) {
            val current = sessions.getValue(this)
            sessions.remove(this)
            sessions.values.forEach {
                it.session.send("[${current.name}] left chat")
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }.collect()
}

data class UserSessionData(
    val name: String,
    val session: WebSocketSession,
)
