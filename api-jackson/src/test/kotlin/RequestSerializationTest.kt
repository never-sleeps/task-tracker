import me.neversleeps.api.jackson.v1.models.IRequest
import me.neversleeps.api.jackson.v1.models.ProjectCreateObject
import me.neversleeps.api.jackson.v1.models.ProjectCreateRequest
import me.neversleeps.api.jackson.v1.models.ProjectDebugStub
import kotlin.test.Test
import kotlin.test.assertContains
import kotlin.test.assertEquals

class RequestSerializationTest {
    private val request = ProjectCreateRequest(
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
        val json = apiMapper.writeValueAsString(request)

        assertContains(json, Regex("\"requestType\":\\s*\"createProject\""))
        assertContains(json, Regex("\"stub\":\\s*\"badTitle\""))
        assertContains(json, Regex("\"title\":\\s*\"some title\""))
        assertContains(json, Regex("\"description\":\\s*\"some description\""))
        assertContains(json, Regex("\"createdBy\":\\s*\"some-author-id\""))
    }

    @Test
    fun deserialize() {
        val json = apiMapper.writeValueAsString(request)
        val obj = apiMapper.readValue(json, IRequest::class.java) as ProjectCreateRequest

        assertEquals(request, obj)
    }

    @Test
    fun deserializeNaked() {
        val jsonString = """
            {"requestId": "12345"}
        """.trimIndent()
        val obj = apiMapper.readValue(jsonString, ProjectCreateRequest::class.java)

        assertEquals("12345", obj.requestId)
    }
}
