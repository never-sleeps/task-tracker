package me.neversleeps.app.stubs.multiplatform

import io.ktor.client.plugins.websocket.* // ktlint-disable no-wildcard-imports
import io.ktor.server.testing.*
import io.ktor.websocket.*
import kotlinx.coroutines.withTimeout
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import me.neversleeps.api.multiplatform.apiMapper
import me.neversleeps.api.multiplatform.v1.models.IRequest
import me.neversleeps.api.multiplatform.v1.models.IResponse
import me.neversleeps.api.multiplatform.v1.models.TaskCreateObject
import me.neversleeps.api.multiplatform.v1.models.TaskCreateRequest
import me.neversleeps.api.multiplatform.v1.models.TaskDebugStub
import me.neversleeps.api.multiplatform.v1.models.TaskPriority
import me.neversleeps.api.multiplatform.v1.models.TaskStatus
import me.neversleeps.api.multiplatform.v1.models.TaskType
import java.util.*
import kotlin.test.Test
import kotlin.test.assertEquals

class V2WebsocketTaskStubTest {

    @Test
    fun createStub() {
        val request = TaskCreateRequest(
            requestType = "createTask",
            requestId = UUID.randomUUID().toString(),
            stub = TaskDebugStub.SUCCESS,
            data = TaskCreateObject(
                type = TaskType.BACKEND,
                priority = TaskPriority.HIGH,
                status = TaskStatus.TODO,
                title = "some title",
                description = "some description",
                executor = UUID.randomUUID().toString(),
                createdBy = UUID.randomUUID().toString(),
            ),
        )

        testMethod<IResponse>(request) {
            assertEquals(request.requestId, it.requestId)
        }
    }

    private inline fun <reified T> testMethod(
        request: IRequest,
        crossinline assertBlock: (T) -> Unit,
    ) = testApplication {
        val client = createClient {
            install(WebSockets)
        }

        client.webSocket("/ws/v2/task") {
            withTimeout(4000) {
                val incame = incoming.receive() as Frame.Text
            }
            send(Frame.Text(apiMapper.encodeToString(request)))
            withTimeout(4000) {
                val incame = incoming.receive() as Frame.Text
                val text = incame.readText()
                val response = apiMapper.decodeFromString<T>(text)

                assertBlock(response)
            }
        }
    }
}
