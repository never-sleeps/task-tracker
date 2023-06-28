package me.neversleeps.mappers.jackson.fromTransport

import me.neversleeps.api.jackson.v1.models.IRequest
import me.neversleeps.common.models.RequestId
import me.neversleeps.common.models.user.UserId

fun IRequest?.toRequestId() = this?.requestId?.let { RequestId(it) } ?: RequestId.NONE

fun String?.toUserId() = this?.let { UserId(it) } ?: UserId.NONE
