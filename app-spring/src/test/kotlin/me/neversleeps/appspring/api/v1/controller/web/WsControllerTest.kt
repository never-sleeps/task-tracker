package me.neversleeps.appspring.api.v1.controller.web

import io.kotest.common.runBlocking
import jakarta.websocket.* // ktlint-disable no-wildcard-imports
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import kotlinx.coroutines.withTimeout
import me.neversleeps.api.jackson.apiMapper
import me.neversleeps.api.jackson.v1.models.IResponse
import me.neversleeps.api.jackson.v1.models.InitBaseResponse
import me.neversleeps.api.jackson.v1.models.ResponseResultStatus
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.server.LocalServerPort
import java.net.URI

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class WsControllerTest {

    @LocalServerPort
    private var port: Int = 0

    private lateinit var container: WebSocketContainer
    private lateinit var client: TestWebSocketClient

    @BeforeEach
    fun setup() {
        container = ContainerProvider.getWebSocketContainer()
        client = TestWebSocketClient()
    }

    @Test
    fun initSessionV1() {
        runBlocking {
            withContext(Dispatchers.IO) {
                container.connectToServer(client, URI.create("ws://localhost:$port/ws/v1/project"))
            }

            withTimeout(3000) {
                while (client.session?.isOpen != true) {
                    delay(100)
                }
            }
            assert(client.session?.isOpen == true)
            withTimeout(3000) {
                val incame = client.receive()
                val message = apiMapper.readValue(incame, IResponse::class.java)
                assert(message is InitBaseResponse)
                assert(message.resultStatus == ResponseResultStatus.SUCCESS)
            }
        }
    }
}

@ClientEndpoint
class TestWebSocketClient {
    var session: Session? = null
    private val messages: MutableList<String> = mutableListOf()

    @OnOpen
    fun onOpen(session: Session?) {
        this.session = session
    }

    @OnClose
    fun onClose(session: Session?, reason: CloseReason) {
        this.session = null
    }

    @OnMessage
    fun onMessage(message: String) {
        messages.add(message)
    }

    suspend fun receive(): String {
        while (messages.isEmpty()) {
            delay(100)
        }
        return messages.removeFirst()
    }
}
