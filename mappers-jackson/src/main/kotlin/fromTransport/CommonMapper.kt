package me.neversleeps.mappers.jackson.fromTransport

import me.neversleeps.api.jackson.v1.models.IRequest
import me.neversleeps.api.jackson.v1.models.WorkMode
import me.neversleeps.common.models.RequestId
import me.neversleeps.common.models.user.UserId

fun IRequest?.toRequestId() = this?.requestId?.let { RequestId(it) } ?: RequestId.NONE

fun String?.toUserId() = this?.let { UserId(it) } ?: UserId.NONE

fun WorkMode?.toInternal(): me.neversleeps.common.stubs.WorkMode = when (this) {
    WorkMode.PROD -> me.neversleeps.common.stubs.WorkMode.PROD
    WorkMode.TEST -> me.neversleeps.common.stubs.WorkMode.TEST
    WorkMode.STUB -> me.neversleeps.common.stubs.WorkMode.STUB
    null -> me.neversleeps.common.stubs.WorkMode.NONE
}
