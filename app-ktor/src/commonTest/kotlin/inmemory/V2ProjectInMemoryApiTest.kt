package inmemory

import ProjectStub // ktlint-disable no-wildcard-imports
import auth.addAuth
import helpers.testSettings
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.server.testing.*
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
import me.neversleeps.app.base.KtorAuthConfig
import me.neversleeps.app.module
import me.neversleeps.common.models.AppLock
import me.neversleeps.common.models.project.ProjectId
import me.neversleeps.common.models.user.UserId
import me.neversleeps.`in`.memory.project.ProjectRepositoryInMemory
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals

class V2ProjectInMemoryApiTest {
    object ProjectPaths {
        const val create = "/api/v2/project/create"
        const val read = "/api/v2/project/read"
        const val update = "/api/v2/project/update"
        const val delete = "/api/v2/project/delete"
        const val search = "/api/v2/project/search"
    }

    private val uuidOld = "10000000-0000-0000-0000-000000000001"
    private val uuidNew = "10000000-0000-0000-0000-000000000002"

    private val initProject = ProjectStub.prepareResult {
        id = ProjectId(uuidOld)
        title = "some title"
        description = "some description"
        createdBy = UserId("some-author")
        lock = AppLock(uuidOld)
    }

    private val userId = initProject.createdBy

    @Test
    fun create() = testApplication {
        application { module(testSettings(ProjectRepositoryInMemory(randomUuid = { uuidNew }))) }

        val data = ProjectCreateObject(
            title = "some title",
            description = "some description",
            createdBy = "some-author",
        )

        val response = client.post(ProjectPaths.create) {
            val requestObj = ProjectCreateRequest(
                requestId = "12345",
                requestType = "create",
                stub = ProjectDebugStub.SUCCESS,
                mode = WorkMode.TEST,
                data = data,
            )
            contentType(ContentType.Application.Json)
            addAuth(id = userId.asString(), config = KtorAuthConfig.TEST)
            val requestJson = apiMapper.encodeToString(requestObj)
            setBody(requestJson)
        }
        val responseJson = response.bodyAsText()
        val responseObj = apiMapper.decodeFromString<ProjectCreateResponse>(responseJson)
        assertEquals(200, response.status.value)
        assertEquals(uuidNew, responseObj.project?.id)
        assertEquals(data.title, responseObj.project?.title)
        assertEquals(data.description, responseObj.project?.description)
    }

    @Test
    fun read() = testApplication {
        val repo = ProjectRepositoryInMemory(initObjects = listOf(initProject), randomUuid = { uuidNew })
        application {
            module(testSettings(repo))
        }

        val response = client.post(ProjectPaths.read) {
            val requestObj = ProjectReadRequest(
                requestId = "12345",
                id = uuidOld,
                mode = WorkMode.TEST,
            )
            contentType(ContentType.Application.Json)
            addAuth(id = userId.asString(), config = KtorAuthConfig.TEST)
            val requestJson = apiMapper.encodeToString(requestObj)
            setBody(requestJson)
        }
        val responseJson = response.bodyAsText()
        val responseObj = apiMapper.decodeFromString<ProjectReadResponse>(responseJson)
        assertEquals(200, response.status.value)
        assertEquals(uuidOld, responseObj.project?.id)
    }

    @Test
    fun update() = testApplication {
        val repo = ProjectRepositoryInMemory(initObjects = listOf(initProject), randomUuid = { uuidNew })
        application {
            module(testSettings(repo))
        }

        val adUpdate = ProjectUpdateObject(
            id = uuidOld,
            title = "some title",
            description = "some description",
        )

        val response = client.post(ProjectPaths.update) {
            val requestObj = ProjectUpdateRequest(
                requestId = "12345",
                data = adUpdate,
                stub = ProjectDebugStub.SUCCESS,
                mode = WorkMode.TEST,
                lock = initProject.lock.asString(),
            )

            contentType(ContentType.Application.Json)
            addAuth(id = userId.asString(), config = KtorAuthConfig.TEST)
            val requestJson = apiMapper.encodeToString(requestObj)
            setBody(requestJson)
        }
        val responseJson = response.bodyAsText()
        val responseObj = apiMapper.decodeFromString<ProjectUpdateResponse>(responseJson)
        assertEquals(200, response.status.value)
        assertEquals(adUpdate.id, responseObj.project?.id)
        assertEquals(adUpdate.title, responseObj.project?.title)
        assertEquals(adUpdate.description, responseObj.project?.description)
    }

    @Test
    fun delete() = testApplication {
        val repo = ProjectRepositoryInMemory(initObjects = listOf(initProject), randomUuid = { uuidNew })
        application {
            module(testSettings(repo))
        }

        val response = client.post(ProjectPaths.delete) {
            val requestObj = ProjectDeleteRequest(
                requestId = "12345",
                id = uuidOld,
                lock = initProject.lock.asString(),
                mode = WorkMode.TEST,
            )
            contentType(ContentType.Application.Json)
            addAuth(id = userId.asString(), config = KtorAuthConfig.TEST)
            val requestJson = apiMapper.encodeToString(requestObj)
            setBody(requestJson)
        }
        val responseJson = response.bodyAsText()
        val responseObj = apiMapper.decodeFromString<ProjectDeleteResponse>(responseJson)
        assertEquals(200, response.status.value)
        assertEquals(uuidOld, responseObj.project?.id)
    }

    @Test
    fun search() = testApplication {
        val repo = ProjectRepositoryInMemory(initObjects = listOf(initProject), randomUuid = { uuidNew })
        application {
            module(testSettings(repo))
        }

        val response = client.post(ProjectPaths.search) {
            val requestObj = ProjectSearchRequest(
                requestId = "12345",
                filter = ProjectSearchFilter(),
                mode = WorkMode.TEST,
            )

            contentType(ContentType.Application.Json)
            addAuth(id = userId.asString(), config = KtorAuthConfig.TEST)
            val requestJson = apiMapper.encodeToString(requestObj)
            setBody(requestJson)
        }
        val responseJson = response.bodyAsText()
        val responseObj = apiMapper.decodeFromString<ProjectSearchResponse>(responseJson)
        assertEquals(200, response.status.value)
        assertNotEquals(0, responseObj.projects?.size)
        assertEquals(uuidOld, responseObj.projects?.first()?.id)
    }
}
