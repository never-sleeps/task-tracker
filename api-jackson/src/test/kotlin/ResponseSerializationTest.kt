import me.neversleeps.api.jackson.v1.models.IResponse
import me.neversleeps.api.jackson.v1.models.ProjectCreateResponse
import me.neversleeps.api.jackson.v1.models.ProjectPermission
import me.neversleeps.api.jackson.v1.models.ProjectResponseObject
import me.neversleeps.api.jackson.v1.models.ResponseResultStatus
import kotlin.test.Test
import kotlin.test.assertContains
import kotlin.test.assertEquals

class ResponseSerializationTest {
    private val response = ProjectCreateResponse(
        requestId = "12345",
        responseType = "createProject",
        resultStatus = ResponseResultStatus.SUCCESS,
        errors = listOf(
            me.neversleeps.api.jackson.v1.models.Error(
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
        val json = apiMapper.writeValueAsString(response)

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
        val json = apiMapper.writeValueAsString(response)
        val obj = apiMapper.readValue(json, IResponse::class.java) as ProjectCreateResponse

        assertEquals(response, obj)
    }
}
