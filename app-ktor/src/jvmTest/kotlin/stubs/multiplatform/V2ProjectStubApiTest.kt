package stubs.multiplatform

import helpers.testSettings
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.* // ktlint-disable no-wildcard-imports
import io.ktor.http.* // ktlint-disable no-wildcard-imports
import io.ktor.server.testing.* // ktlint-disable no-wildcard-imports
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import me.neversleeps.api.multiplatform.apiMapper
import me.neversleeps.api.multiplatform.v1.models.ProjectCreateObject
import me.neversleeps.api.multiplatform.v1.models.ProjectCreateRequest
import me.neversleeps.api.multiplatform.v1.models.ProjectCreateResponse
import me.neversleeps.api.multiplatform.v1.models.ProjectDebugStub
import me.neversleeps.api.multiplatform.v1.models.ProjectDeleteRequest
import me.neversleeps.api.multiplatform.v1.models.ProjectDeleteResponse
import me.neversleeps.api.multiplatform.v1.models.ProjectReadRequest
import me.neversleeps.api.multiplatform.v1.models.ProjectReadResponse
import me.neversleeps.api.multiplatform.v1.models.ProjectSearchFilter
import me.neversleeps.api.multiplatform.v1.models.ProjectSearchRequest
import me.neversleeps.api.multiplatform.v1.models.ProjectSearchResponse
import me.neversleeps.api.multiplatform.v1.models.ProjectUpdateObject
import me.neversleeps.api.multiplatform.v1.models.ProjectUpdateRequest
import me.neversleeps.api.multiplatform.v1.models.ProjectUpdateResponse
import me.neversleeps.api.multiplatform.v1.models.WorkMode
import me.neversleeps.app.module
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

class V2ProjectStubApiTest {
    object ProjectPaths {
        const val create = "/api/v2/project/create"
        const val read = "/api/v2/project/read"
        const val update = "/api/v2/project/update"
        const val delete = "/api/v2/project/delete"
        const val search = "/api/v2/project/search"
    }

    @Test
    fun create() = testApplication {
        // given when
        application { module(testSettings()) }
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
            contentType(ContentType.Application.Json)
            val requestJson = apiMapper.encodeToString(requestObj)
            setBody(requestJson)
        }
        val responseJson = response.bodyAsText()
        val responseObj = apiMapper.decodeFromString<ProjectCreateResponse>(responseJson)

        // then
        assertEquals(200, response.status.value)
        assertEquals("03e13b55-b5b2-484d-a08b-b18aeb087c88", responseObj.project?.id) // data of stubs
        assertEquals("8098d197-a58f-4ae4-b602-8db6a146fb17", responseObj.project?.createdBy) // data of stubs
    }

    @Test
    fun read() = testApplication {
        // given when
        application { module(testSettings()) }
        val response = client.post(ProjectPaths.read) {
            val requestObj = ProjectReadRequest(
                requestId = "12345",
                id = "03e13b55-b5b2-484d-a08b-b18aeb087c88",
                stub = ProjectDebugStub.SUCCESS,
                mode = WorkMode.STUB,
            )
            contentType(ContentType.Application.Json)
            val requestJson = apiMapper.encodeToString(requestObj)
            setBody(requestJson)
        }
        val responseJson = response.bodyAsText()
        val responseObj = apiMapper.decodeFromString<ProjectReadResponse>(responseJson)

        // then
        assertEquals(200, response.status.value)
        assertEquals("03e13b55-b5b2-484d-a08b-b18aeb087c88", responseObj.project?.id) // data of stubs
        assertEquals("8098d197-a58f-4ae4-b602-8db6a146fb17", responseObj.project?.createdBy) // data of stubs
    }

    @Test
    fun update() = testApplication {
        // given when
        application { module(testSettings()) }
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
            contentType(ContentType.Application.Json)
            val requestJson = apiMapper.encodeToString(requestObj)
            setBody(requestJson)
        }
        val responseJson = response.bodyAsText()
        val responseObj = apiMapper.decodeFromString<ProjectUpdateResponse>(responseJson)

        // then
        assertEquals(200, response.status.value)
        assertEquals("03e13b55-b5b2-484d-a08b-b18aeb087c88", responseObj.project?.id) // data of stubs
        assertEquals("8098d197-a58f-4ae4-b602-8db6a146fb17", responseObj.project?.createdBy) // data of stubs
    }

    @Test
    fun delete() = testApplication {
        // given when
        application { module(testSettings()) }
        val response = client.post(ProjectPaths.delete) {
            val requestObj = ProjectDeleteRequest(
                requestId = "12345",
                id = "03e13b55-b5b2-484d-a08b-b18aeb087c88",
                stub = ProjectDebugStub.SUCCESS,
                mode = WorkMode.STUB,
            )
            contentType(ContentType.Application.Json)
            val requestJson = apiMapper.encodeToString(requestObj)
            setBody(requestJson)
        }
        val responseJson = response.bodyAsText()
        val responseObj = apiMapper.decodeFromString<ProjectDeleteResponse>(responseJson)

        // then
        assertEquals(200, response.status.value)
        assertEquals("03e13b55-b5b2-484d-a08b-b18aeb087c88", responseObj.project?.id) // data of stubs
        assertEquals("8098d197-a58f-4ae4-b602-8db6a146fb17", responseObj.project?.createdBy) // data of stubs
    }

    @Test
    fun search() = testApplication {
        // given when
        application { module(testSettings()) }
        val response = client.post(ProjectPaths.search) {
            val requestObj = ProjectSearchRequest(
                requestId = "12345",
                stub = ProjectDebugStub.SUCCESS,
                mode = WorkMode.STUB,
                filter = ProjectSearchFilter(searchText = "some project title"),
            )
            contentType(ContentType.Application.Json)
            val requestJson = apiMapper.encodeToString(requestObj)
            setBody(requestJson)
        }
        val responseJson = response.bodyAsText()
        val responseObj = apiMapper.decodeFromString<ProjectSearchResponse>(responseJson)

        // then
        assertEquals(200, response.status.value)
        assertEquals(5, responseObj.projects?.size)

        assertNotNull(responseObj.projects?.find { it.id == "PRO-101" })
    }
}
