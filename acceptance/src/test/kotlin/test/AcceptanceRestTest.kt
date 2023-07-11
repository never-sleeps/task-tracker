package me.neversleeps.acceptance.blackbox.test

import io.kotest.core.annotation.Ignored
import me.neversleeps.acceptance.blackbox.docker.KtorDockerCompose
import me.neversleeps.acceptance.blackbox.docker.SpringDockerCompose
import me.neversleeps.acceptance.blackbox.fixture.BaseFunSpec
import me.neversleeps.acceptance.blackbox.fixture.client.RestClient
import me.neversleeps.acceptance.blackbox.fixture.client.WebSocketClient
import me.neversleeps.acceptance.blackbox.fixture.docker.DockerCompose

@Ignored
open class AcceptanceRestTestBase(dockerCompose: DockerCompose, runWebSocket: Boolean = false) : BaseFunSpec(
    dockerCompose,
    {
        val restClient = RestClient(dockerCompose)
        testApiV1(restClient, "rest ")
        testApiV2(restClient, "rest ")

        if (runWebSocket) {
            val websocketClient = WebSocketClient(dockerCompose)
            testApiV1(websocketClient, "websocket ")
            testApiV2(websocketClient, "websocket ")
        }
    },
)

class AcceptanceRestSpringTest : AcceptanceRestTestBase(SpringDockerCompose, true)

// пока websocket-тесты не работают с ktor
class AcceptanceRestKtorTest : AcceptanceRestTestBase(KtorDockerCompose)
