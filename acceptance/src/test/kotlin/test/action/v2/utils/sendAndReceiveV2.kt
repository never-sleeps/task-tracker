package me.neversleeps.acceptance.blackbox.test.action.v2.utils // ktlint-disable filename

import me.neversleeps.acceptance.blackbox.fixture.client.Client
import me.neversleeps.api.multiplatform.apiRequestSerialize
import me.neversleeps.api.multiplatform.apiResponseDeserialize
import me.neversleeps.api.multiplatform.v1.models.IRequest
import me.neversleeps.api.multiplatform.v1.models.IResponse
import mu.KotlinLogging

private val log = KotlinLogging.logger {}

suspend fun Client.sendAndReceive(path: String, request: IRequest): IResponse {
    val requestBody = apiRequestSerialize(request)
    log.warn { "Send to v2/$path\n$requestBody" }

    val responseBody = sendAndReceive("v2", path, requestBody)
    log.warn { "Received:\n$responseBody" }

    return apiResponseDeserialize(responseBody)
}
