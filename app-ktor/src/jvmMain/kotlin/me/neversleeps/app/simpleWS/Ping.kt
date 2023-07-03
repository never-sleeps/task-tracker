package me.neversleeps.app.simpleWS

import io.ktor.websocket.* // ktlint-disable no-wildcard-imports

suspend fun DefaultWebSocketSession.wsPing() {
    send("Please enter your name")

    for (frame in incoming) {
        if (frame !is Frame.Text) {
            continue
        }

        val name = frame.readText()
        send(Frame.Text("Hello $name"))
    }
}
