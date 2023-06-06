import kotlinx.datetime.Instant
import models.AppCommand
import models.AppError
import models.AppState
import models.RequestId

interface IContext {
    var requestId: RequestId
    var timeStart: Instant
    var command: AppCommand
    var state: AppState

    var errors: MutableList<AppError>
}
