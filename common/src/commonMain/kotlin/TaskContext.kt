import kotlinx.datetime.Instant
import models.AppCommand
import models.AppError
import models.AppState
import models.RequestId
import stubs.TaskDebugStub
import models.task.Task
import models.task.TaskSearchFilter

data class TaskContext(
    override var requestId: RequestId = RequestId.NONE,
    override var timeStart: Instant = Instant.NONE,
    override var command: AppCommand = AppCommand.NONE,
    override var state: AppState = AppState.NONE,
    override var errors: MutableList<AppError> = mutableListOf(),

    var taskRequest: Task = Task(),
    var taskSearchFilterRequest: TaskSearchFilter = TaskSearchFilter(),
    var taskResponse: Task = Task(),
    var tasksResponse: MutableList<Task> = mutableListOf(),
    var stubCase: TaskDebugStub = TaskDebugStub.NONE,
) : IContext
