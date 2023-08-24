package stubs.jackson

import auth.addAuth
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.SerializationFeature
import helpers.testSettings
import io.ktor.client.call.body
import io.ktor.client.plugins.contentnegotiation.* // ktlint-disable no-wildcard-imports
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.* // ktlint-disable no-wildcard-imports
import io.ktor.serialization.jackson.* // ktlint-disable no-wildcard-imports
import io.ktor.server.testing.* // ktlint-disable no-wildcard-imports
import me.neversleeps.api.jackson.v1.models.ProjectCreateObject
import me.neversleeps.api.jackson.v1.models.ProjectCreateRequest
import me.neversleeps.api.jackson.v1.models.ProjectCreateResponse
import me.neversleeps.api.jackson.v1.models.ProjectDebugStub
import me.neversleeps.api.jackson.v1.models.ProjectDeleteRequest
import me.neversleeps.api.jackson.v1.models.ProjectDeleteResponse
import me.neversleeps.api.jackson.v1.models.ProjectReadRequest
import me.neversleeps.api.jackson.v1.models.ProjectReadResponse
import me.neversleeps.api.jackson.v1.models.ProjectSearchFilter
import me.neversleeps.api.jackson.v1.models.ProjectSearchRequest
import me.neversleeps.api.jackson.v1.models.ProjectSearchResponse
import me.neversleeps.api.jackson.v1.models.ProjectUpdateObject
import me.neversleeps.api.jackson.v1.models.ProjectUpdateRequest
import me.neversleeps.api.jackson.v1.models.ProjectUpdateResponse
import me.neversleeps.api.jackson.v1.models.WorkMode
import me.neversleeps.app.base.KtorAuthConfig
import me.neversleeps.app.moduleJvm
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

class V1ProjectStubApiTest {
    object ProjectPaths {
        const val create = "/api/v1/project/create"
        const val read = "/api/v1/project/read"
        const val update = "/api/v1/project/update"
        const val delete = "/api/v1/project/delete"
        const val search = "/api/v1/project/search"
    }

    @Test
    fun create() = testApplication {
        application {
            moduleJvm(appSettings = testSettings())
        }
        // given when
        val client = myClient()

        val response = client.post(ProjectPaths.create) {
            val requestObj = ProjectCreateRequest(
                requestId = "12345",
                stub = ProjectDebugStub.SUCCESS,
                mode = WorkMode.STUB,
                data = ProjectCreateObject(
                    title = "some title",
                    description = "some description",

                ),
            )
            addAuth(config = KtorAuthConfig.TEST)
            contentType(ContentType.Application.Json)
            setBody(requestObj)
        }
        val responseObj = response.body<ProjectCreateResponse>()
        println(responseObj)

        // then
        assertEquals(200, response.status.value)
        assertEquals("03e13b55-b5b2-484d-a08b-b18aeb087c88", responseObj.project?.id) // data of stubs
        assertEquals("8098d197-a58f-4ae4-b602-8db6a146fb17", responseObj.project?.createdBy) // data of stubs
    }

    @Test
    fun read() = testApplication {
        application {
            moduleJvm(appSettings = testSettings())
        }
        // given when
        val client = myClient()

        val response = client.post(ProjectPaths.read) {
            val requestObj = ProjectReadRequest(
                requestId = "12345",
                id = "03e13b55-b5b2-484d-a08b-b18aeb087c88",
                stub = ProjectDebugStub.SUCCESS,
                mode = WorkMode.STUB,
            )
            addAuth(config = KtorAuthConfig.TEST)
            contentType(ContentType.Application.Json)
            setBody(requestObj)
        }
        val responseObj = response.body<ProjectReadResponse>()

        // then
        assertEquals(200, response.status.value)
        assertEquals("03e13b55-b5b2-484d-a08b-b18aeb087c88", responseObj.project?.id) // data of stubs
        assertEquals("8098d197-a58f-4ae4-b602-8db6a146fb17", responseObj.project?.createdBy) // data of stubs
    }

    @Test
    fun update() = testApplication {
        application {
            moduleJvm(appSettings = testSettings())
        }
        // given when
        val client = myClient()

        val response = client.post(ProjectPaths.update) {
            val requestObj = ProjectUpdateRequest(
                requestId = "12345",
                stub = ProjectDebugStub.SUCCESS,
                mode = WorkMode.STUB,
                data = ProjectUpdateObject(
                    title = "some title",
                    description = "some description",
                ),
            )
            addAuth(config = KtorAuthConfig.TEST)
            contentType(ContentType.Application.Json)
            setBody(requestObj)
        }
        val responseObj = response.body<ProjectUpdateResponse>()

        // then
        assertEquals(200, response.status.value)
        assertEquals("03e13b55-b5b2-484d-a08b-b18aeb087c88", responseObj.project?.id) // data of stubs
        assertEquals("8098d197-a58f-4ae4-b602-8db6a146fb17", responseObj.project?.createdBy) // data of stubs
    }

    @Test
    fun delete() = testApplication {
        application {
            moduleJvm(appSettings = testSettings())
        }
        // given when
        val client = myClient()

        val response = client.post(ProjectPaths.delete) {
            val requestObj = ProjectDeleteRequest(
                requestId = "12345",
                id = "03e13b55-b5b2-484d-a08b-b18aeb087c88",
                stub = ProjectDebugStub.SUCCESS,
                mode = WorkMode.STUB,
            )
            addAuth(config = KtorAuthConfig.TEST)
            contentType(ContentType.Application.Json)
            setBody(requestObj)
        }
        val responseObj = response.body<ProjectDeleteResponse>()

        // then
        assertEquals(200, response.status.value)
        assertEquals("03e13b55-b5b2-484d-a08b-b18aeb087c88", responseObj.project?.id) // data of stubs
        assertEquals("8098d197-a58f-4ae4-b602-8db6a146fb17", responseObj.project?.createdBy) // data of stubs
    }

    @Test
    fun search() = testApplication {
        application {
            moduleJvm(appSettings = testSettings())
        }
        // given when
        val client = myClient()

        val response = client.post(ProjectPaths.search) {
            val requestObj = ProjectSearchRequest(
                requestId = "12345",
                stub = ProjectDebugStub.SUCCESS,
                mode = WorkMode.STUB,
                filter = ProjectSearchFilter(searchText = "some project title"),
            )
            addAuth(config = KtorAuthConfig.TEST)
            contentType(ContentType.Application.Json)
            setBody(requestObj)
        }
        val responseObj = response.body<ProjectSearchResponse>()

        // then
        assertEquals(200, response.status.value)
        assertEquals(5, responseObj.projects?.size)

        assertNotNull(responseObj.projects?.find { it.id == "PRO-101" })
    }

    private fun ApplicationTestBuilder.myClient() = createClient {
        install(ContentNegotiation) {
            jackson {
                disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)

                enable(SerializationFeature.INDENT_OUTPUT)
                writerWithDefaultPrettyPrinter()
            }
        }
    }
}
