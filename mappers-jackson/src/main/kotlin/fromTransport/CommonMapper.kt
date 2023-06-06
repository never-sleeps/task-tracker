package fromTransport

import me.neversleeps.api.jackson.v1.models.IRequest
import models.RequestId
import models.user.UserId

fun IRequest?.toRequestId() = this?.requestId?.let { RequestId(it) } ?: RequestId.NONE

fun String?.toUserId() = this?.let { UserId(it) } ?: UserId.NONE
