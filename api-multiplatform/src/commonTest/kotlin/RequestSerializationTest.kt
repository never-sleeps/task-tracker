import me.neversleeps.api.multiplatform.v1.models.IRequest
import me.neversleeps.api.multiplatform.v1.models.ProjectCreateObject
import me.neversleeps.api.multiplatform.v1.models.ProjectCreateRequest
import me.neversleeps.api.multiplatform.v1.models.ProjectDebugStub
import kotlin.test.Test
import kotlin.test.assertContains
import kotlin.test.assertEquals

class RequestSerializationTest {
    private val request: IRequest = ProjectCreateRequest(
        requestId = "12345",
        requestType = "createProject",
        stub = ProjectDebugStub.BAD_TITLE,
        data = ProjectCreateObject(
            title = "some title",
            description = "some description",
            createdBy = "some-author-id",
        ),
    )

    @Test
    fun serialize() {
        val json = apiRequestSerialize(request)
        println(json)

        assertContains(json, Regex("\"requestType\":\\s*\"createProject\""))
        assertContains(json, Regex("\"stub\":\\s*\"badTitle\""))
        assertContains(json, Regex("\"title\":\\s*\"some title\""))
        assertContains(json, Regex("\"description\":\\s*\"some description\""))
        assertContains(json, Regex("\"createdBy\":\\s*\"some-author-id\""))
    }

    @Test
    fun serializeWithoutType() {
        val json = apiRequestSerialize((request as ProjectCreateRequest).copy(requestType = null))
        println(json)

        assertContains(json, Regex("\"requestType\":\\s*\"createProject\""))
        assertContains(json, Regex("\"stub\":\\s*\"badTitle\""))
        assertContains(json, Regex("\"title\":\\s*\"some title\""))
        assertContains(json, Regex("\"description\":\\s*\"some description\""))
        assertContains(json, Regex("\"createdBy\":\\s*\"some-author-id\""))
    }

    @Test
    fun deserialize() {
        val json = apiRequestSerialize(request)
        val obj = apiRequestDeserialize(json) as ProjectCreateRequest

        assertEquals(request, obj)
    }

    @Test
    fun deserializeNaked() {
        val jsonString = """
            {
                "requestType": "createProject",
                "requestId": "12345",
                "stub": "badTitle",
                "data": {
                    "title": "some title",
                    "description": "some description",
                    "createdBy": "some-author-id"
                }
            }
        """.trimIndent()
        val obj = apiRequestDeserialize(jsonString) as IRequest

        assertEquals("12345", obj.requestId)
        assertEquals("createProject", obj.requestType)
        assertEquals(request, obj)
    }
}
