package me.neversleeps.acceptance.blackbox.test.action.v1.utils // ktlint-disable filename

import me.neversleeps.acceptance.blackbox.fixture.client.Client
import me.neversleeps.api.jackson.apiRequestSerialize
import me.neversleeps.api.jackson.apiResponseDeserialize
import me.neversleeps.api.jackson.v1.models.IRequest
import me.neversleeps.api.jackson.v1.models.IResponse
import me.neversleeps.api.multiplatform.v1.models.ProjectCreateRequest
import mu.KotlinLogging

private val log = KotlinLogging.logger {}

suspend fun Client.sendAndReceive(path: String, request: IRequest): IResponse {
    val requestBody = apiRequestSerialize(request)
    log.warn { "Send to v1/$path\n$requestBody" }

    val responseBody = sendAndReceive("v1", path, requestBody)
    log.info { "Received:\n$responseBody" }

    return apiResponseDeserialize(responseBody)
}
