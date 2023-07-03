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
import me.neversleeps.api.multiplatform.v1.models.ProjectCreateObject
import me.neversleeps.api.multiplatform.v1.models.ProjectCreateRequest
import me.neversleeps.api.multiplatform.v1.models.ProjectDebugStub
import me.neversleeps.api.multiplatform.v1.models.ProjectDeleteRequest
import me.neversleeps.api.multiplatform.v1.models.ProjectReadRequest
import me.neversleeps.api.multiplatform.v1.models.ProjectSearchFilter
import me.neversleeps.api.multiplatform.v1.models.ProjectSearchRequest
import me.neversleeps.api.multiplatform.v1.models.ProjectUpdateObject
import me.neversleeps.api.multiplatform.v1.models.ProjectUpdateRequest
import kotlin.test.Test
import kotlin.test.assertEquals

class V2WebsocketProjectStubTest {

    @Test
    fun createStub() {
        val request = ProjectCreateRequest(
            requestType = "createProject",
            requestId = "12345",
            stub = ProjectDebugStub.SUCCESS,
            data = ProjectCreateObject(
                title = "some title",
                description = "some description",

            ),
        )

        testMethod<IResponse>(request) {
            assertEquals("12345", it.requestId)
        }
    }

    @Test
    fun readStub() {
        val request = ProjectReadRequest(
            requestId = "12345",
            id = "03e13b55-b5b2-484d-a08b-b18aeb087c88",
            stub = ProjectDebugStub.SUCCESS,
        )

        testMethod<IResponse>(request) {
            assertEquals("12345", it.requestId)
        }
    }

    @Test
    fun updateStub() {
        val request = ProjectUpdateRequest(
            requestId = "12345",
            stub = ProjectDebugStub.SUCCESS,
            data = ProjectUpdateObject(
                title = "some title",
                description = "some description",
            ),
        )

        testMethod<IResponse>(request) {
            assertEquals("12345", it.requestId)
        }
    }

    @Test
    fun deleteStub() {
        val request = ProjectDeleteRequest(
            requestId = "12345",
            id = "03e13b55-b5b2-484d-a08b-b18aeb087c88",
            stub = ProjectDebugStub.SUCCESS,
        )

        testMethod<IResponse>(request) {
            assertEquals("12345", it.requestId)
        }
    }

    @Test
    fun searchStub() {
        val request = ProjectSearchRequest(
            requestId = "12345",
            stub = ProjectDebugStub.SUCCESS,
            filter = ProjectSearchFilter(searchText = "some project title"),
        )

        testMethod<IResponse>(request) {
            assertEquals("12345", it.requestId)
        }
    }

    private inline fun <reified T> testMethod(
        request: IRequest,
        crossinline assertBlock: (T) -> Unit,
    ) = testApplication {
        val client = createClient {
            install(WebSockets)
        }

        client.webSocket("/ws/v2/project") {
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
