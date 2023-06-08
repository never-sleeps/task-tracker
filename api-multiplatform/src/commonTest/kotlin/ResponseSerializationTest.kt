package me.neversleeps.api.multiplatform

import me.neversleeps.api.multiplatform.v1.models.IResponse
import me.neversleeps.api.multiplatform.v1.models.ProjectCreateResponse
import me.neversleeps.api.multiplatform.v1.models.ProjectPermission
import me.neversleeps.api.multiplatform.v1.models.ProjectResponseObject
import me.neversleeps.api.multiplatform.v1.models.ResponseResultStatus
import kotlin.test.Test
import kotlin.test.assertContains
import kotlin.test.assertEquals

class ResponseSerializationTest {

    private val response: IResponse = ProjectCreateResponse(
        requestId = "12345",
        responseType = "createProject",
        resultStatus = ResponseResultStatus.SUCCESS,
        errors = listOf(
            me.neversleeps.api.multiplatform.v1.models.Error(
                code = "some code",
                group = "some group",
                field = "some field",
                message = "some message",
            ),
        ),
        project = ProjectResponseObject(
            title = "some title",
            description = "some description",
            createdBy = "some-author-id",
            id = "some id",
            permissions = setOf(ProjectPermission.READ, ProjectPermission.UPDATE),
        ),
    )

    @Test
    fun serialize() {
        val json = apiResponseSerialize(response)
        println(json)

        assertContains(json, Regex("\"requestId\":\\s*\"12345\""))
        assertContains(json, Regex("\"responseType\":\\s*\"createProject\""))
        assertContains(json, Regex("\"code\":\\s*\"some code\""))
        assertContains(json, Regex("\"group\":\\s*\"some group\""))
        assertContains(json, Regex("\"field\":\\s*\"some field\""))
        assertContains(json, Regex("\"message\":\\s*\"some message\""))
        assertContains(json, Regex("\"title\":\\s*\"some title\""))
        assertContains(json, Regex("\"description\":\\s*\"some description\""))
        assertContains(json, Regex("\"createdBy\":\\s*\"some-author-id\""))
        assertContains(json, Regex("\"id\":\\s*\"some id\""))
    }

    @Test
    fun deserialize() {
        val json = apiResponseSerialize(response)
        val obj = apiResponseDeserialize(json) as ProjectCreateResponse

        assertEquals(response, obj)
    }

    @Test
    fun deserializeNaked() {
        val jsonString = """
            {
                "responseType": "createProject",
                "requestId": "12345",
                "resultStatus": "success",
                "errors": [
                    {
                        "code": "some code",
                        "group": "some group",
                        "field": "some field",
                        "message": "some message"
                    }
                ],
                "project": {
                    "title": "some title",
                    "description": "some description",
                    "createdBy": "some-author-id",
                    "id": "some id",
                    "permissions": [
                        "read",
                        "update"
                    ]
                }
            }
        """.trimIndent()
        val obj = apiResponseDeserialize(jsonString) as IResponse

        assertEquals("12345", obj.requestId)
        assertEquals("createProject", obj.responseType)
        assertEquals("success", obj.resultStatus?.name?.lowercase())
        assertEquals("some code", obj.errors?.get(0)?.code)
        assertEquals("some group", obj.errors?.get(0)?.group)
        assertEquals("some field", obj.errors?.get(0)?.field)
        assertEquals("some message", obj.errors?.get(0)?.message)
        assertEquals(response, obj)
    }
}
