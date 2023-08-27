package repository

import com.fasterxml.jackson.databind.DeserializationFeature // ktlint-disable no-wildcard-imports
import com.fasterxml.jackson.databind.SerializationFeature
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.jackson.*
import io.ktor.server.testing.*
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
import org.junit.Test
import kotlin.test.Ignore
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

@Ignore
class V1ProjectInmemoryApiTest {
    companion object {
        const val COMMON_REQUEST_ID = "12345"
        object ApiProjectPaths {
            const val create = "/api/v1/project/create"
            const val read = "/api/v1/project/read"
            const val update = "/api/v1/project/update"
            const val delete = "/api/v1/project/delete"
            const val search = "/api/v1/project/search"
        }
    }

    private val projectCreateRequest = ProjectCreateRequest(
        requestId = "12345",
        requestType = "createProject",
        stub = ProjectDebugStub.SUCCESS,
        mode = WorkMode.STUB,
        data = ProjectCreateObject(
            title = "some title",
            description = "some description",
            createdBy = "some-author-id",
        ),
    )

    @Test
    fun create() = testApplication {
        val client = myClient()
        val responseObj = initObject(client)
        assertEquals(projectCreateRequest.data?.title, responseObj.project?.title)
        assertEquals(projectCreateRequest.data?.description, responseObj.project?.description)
    }

    @Test
    fun read() = testApplication {
        val client = myClient()
        val id = initObject(client).project?.id
        val response = client.post(ApiProjectPaths.read) {
            val requestObj = ProjectReadRequest(
                requestId = COMMON_REQUEST_ID,
                stub = ProjectDebugStub.SUCCESS,
                mode = WorkMode.STUB,
            )
            contentType(ContentType.Application.Json)
            setBody(requestObj)
        }
        val responseObj = response.body<ProjectReadResponse>()
        assertEquals(200, response.status.value)
        assertEquals(id, responseObj.project?.id)
    }

    @Test
    fun update() = testApplication {
        val client = myClient()

        val created = initObject(client)

        val data = ProjectUpdateObject(
            id = created.project?.id,
            title = "Болт",
            description = "КРУТЕЙШИЙ",
            createdBy = "someone-id",
        )

        val response = client.post(ApiProjectPaths.update) {
            val requestObj = ProjectUpdateRequest(
                requestId = COMMON_REQUEST_ID,
                data = data,
                stub = ProjectDebugStub.SUCCESS,
                mode = WorkMode.STUB,
                lock = created.project?.lock,
            )
            contentType(ContentType.Application.Json)
            setBody(requestObj)
        }
        val responseObj = response.body<ProjectUpdateResponse>()
        assertEquals(200, response.status.value)
        assertEquals(data.id, responseObj.project?.id)
        assertEquals(data.title, responseObj.project?.title)
        assertEquals(data.description, responseObj.project?.description)
        assertEquals(data.createdBy, responseObj.project?.createdBy)
    }

    @Test
    fun delete() = testApplication {
        val client = myClient()
        val created = initObject(client)
        val oldId = created.project?.id

        val response = client.post(ApiProjectPaths.delete) {
            val requestObj = ProjectDeleteRequest(
                requestId = COMMON_REQUEST_ID,
                id = oldId,
                stub = ProjectDebugStub.SUCCESS,
                mode = WorkMode.STUB,
                lock = created.project?.lock,
            )
            contentType(ContentType.Application.Json)
            setBody(requestObj)
        }
        val responseObj = response.body<ProjectDeleteResponse>()
        assertEquals(200, response.status.value)
        assertEquals(created.project?.id, responseObj.project?.id)
    }

    @Test
    fun search() = testApplication {
        val client = myClient()
        val response = client.post(ApiProjectPaths.search) {
            val requestObj = ProjectSearchRequest(
                requestId = "12345",
                filter = ProjectSearchFilter(searchText = "some title", createdBy = "someone"),
                stub = ProjectDebugStub.SUCCESS,
                mode = WorkMode.STUB,
            )
            contentType(ContentType.Application.Json)
            setBody(requestObj)
        }
        val responseObj = response.body<ProjectSearchResponse>()
        assertEquals(200, response.status.value)
        assertEquals("success", responseObj.resultStatus?.value)
        assertEquals(5, responseObj.projects?.size)
        assertNotNull(responseObj.projects?.find { it.id == "PRO-101" })
    }

    private suspend fun initObject(client: HttpClient): ProjectCreateResponse {
        val responseСreate = client.post(ApiProjectPaths.create) {
            contentType(ContentType.Application.Json)
            setBody(projectCreateRequest)
        }
        assertEquals(200, responseСreate.status.value)
        return responseСreate.body<ProjectCreateResponse>()
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
