import me.neversleeps.common.ProjectContext
import me.neversleeps.common.models.AppCommand
import me.neversleeps.common.models.AppError
import me.neversleeps.common.models.AppState
import me.neversleeps.common.models.RequestId
import me.neversleeps.common.models.project.Project
import me.neversleeps.common.models.user.UserId
import me.neversleeps.mappers.log1.toLog
import kotlin.test.Test
import kotlin.test.assertEquals

class MapperTest {

    @Test
    fun fromContext() {
        val context = ProjectContext(
            requestId = RequestId("1234"),
            command = AppCommand.CREATE,
            projectResponse = Project(
                title = "some title",
                description = "some description",
                createdBy = UserId("some-author-id"),
            ),
            errors = mutableListOf(
                AppError(
                    code = "some code",
                    group = "some group",
                    field = "some field",
                    message = "some message",
                ),
            ),
            state = AppState.RUNNING,
        )

        val log = context.toLog("test-logid")

        assertEquals("test-logid", log.logId)
        assertEquals("task-tracker", log.source)
        assertEquals("1234", log.project?.requestId)
        assertEquals("some title", log.project?.responseProject?.title)
        assertEquals("some description", log.project?.responseProject?.description)

        val error = log.errors?.firstOrNull()
        assertEquals("some code", error?.code)
        assertEquals("some field", error?.field)
        assertEquals("some message", error?.message)
        assertEquals("ERROR", error?.level)
    }
}
