package stubs.jackson

import io.ktor.client.plugins.websocket.* // ktlint-disable no-wildcard-imports
import io.ktor.server.testing.*
import io.ktor.websocket.*
import kotlinx.coroutines.withTimeout
import me.neversleeps.api.jackson.apiMapper
import me.neversleeps.api.jackson.v1.models.IResponse
import me.neversleeps.api.jackson.v1.models.InitBaseResponse
import me.neversleeps.api.jackson.v1.models.TaskCreateObject
import me.neversleeps.api.jackson.v1.models.TaskCreateRequest
import me.neversleeps.api.jackson.v1.models.TaskDebugStub
import me.neversleeps.api.jackson.v1.models.TaskPriority
import me.neversleeps.api.jackson.v1.models.TaskStatus
import me.neversleeps.api.jackson.v1.models.TaskType
import java.util.*
import kotlin.test.Ignore
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs

@Ignore
class V1WebsocketTaskStubTest {

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
        request: Any,
        crossinline assertBlock: (T) -> Unit,
    ) = testApplication {
        val client = createClient {
            install(WebSockets)
        }

        client.webSocket("/ws/v1/task") {
            withTimeout(3000) {
                val incame = incoming.receive() as Frame.Text
                val response = apiMapper.readValue(incame.readText(), T::class.java)
                assertIs<InitBaseResponse>(response)
            }
            send(Frame.Text(apiMapper.writeValueAsString(request)))
            withTimeout(3000) {
                val incame = incoming.receive() as Frame.Text
                val response = apiMapper.readValue(incame.readText(), T::class.java)

                assertBlock(response)
            }
        }
    }
}
