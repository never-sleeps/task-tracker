package me.neversleeps.acceptance

import io.ktor.client.* // ktlint-disable no-wildcard-imports
import io.ktor.client.call.* // ktlint-disable no-wildcard-imports
import io.ktor.client.engine.okhttp.* // ktlint-disable no-wildcard-imports
import io.ktor.client.request.* // ktlint-disable no-wildcard-imports
import io.ktor.http.* // ktlint-disable no-wildcard-imports
import me.neversleeps.api.multiplatform.apiRequestSerialize
import me.neversleeps.api.multiplatform.apiResponseDeserialize
import me.neversleeps.api.multiplatform.v1.models.IRequest
import me.neversleeps.api.multiplatform.v1.models.IResponse
import mu.KotlinLogging

private val log = KotlinLogging.logger {}

object RestClient {
    private val client = HttpClient(OkHttp)
    suspend fun request(path: String, request: IRequest): IResponse {
        val url = "http://${AppCompose.C.hostApp}:${AppCompose.C.portApp}/$path"
        val jsonBody = apiRequestSerialize(request)

        log.info { "Send request to $url. Body $jsonBody" }

        val response = client.post {
            url(url)
            headers { append(HttpHeaders.ContentType, ContentType.Application.Json) }
            accept(ContentType.Application.Json)
            setBody(jsonBody)
        }.call

        val body: String = response.body()

        log.info { "Received status: ${response.response.status}, body: $body" }

        return apiResponseDeserialize(body)
    }
}
