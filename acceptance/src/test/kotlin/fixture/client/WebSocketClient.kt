package me.neversleeps.acceptance.blackbox.fixture.client

import io.ktor.client.* // ktlint-disable no-wildcard-imports
import io.ktor.client.engine.okhttp.*
import io.ktor.client.plugins.websocket.*
import io.ktor.http.*
import io.ktor.websocket.*
import kotlinx.coroutines.withTimeout
import me.neversleeps.acceptance.blackbox.fixture.docker.DockerCompose

/**
 * Отправка запросов по http/websocket
 */
class WebSocketClient(dockerCompose: DockerCompose) : Client {
    private val urlBuilder by lazy { dockerCompose.inputUrl }
    private val client = HttpClient(OkHttp) {
        install(WebSockets)
    }

    override suspend fun sendAndReceive(version: String, path: String, request: String): String {
        val url = urlBuilder.apply {
            protocol = URLProtocol.WS
            path("ws/$version/project")
        }.build().toString()

        var response = ""
        client.webSocket(url) {
            withTimeout(3000) {
                val income = incoming.receive() as Frame.Text
                val data = income.readText() // init message - игнорим
            }
            send(Frame.Text(request))

            withTimeout(5000) {
                val income = incoming.receive() as Frame.Text
                response = income.readText()
            }
        }

        return response
    }
}
