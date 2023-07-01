package me.neversleeps.utils // ktlint-disable filename

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import kotlinx.datetime.Clock
import kotlinx.serialization.encodeToString
import me.neversleeps.api.multiplatform.apiMapper
import me.neversleeps.common.ProjectContext
import me.neversleeps.common.TaskContext
import me.neversleeps.common.models.RequestId
import me.neversleeps.model.Request
import me.neversleeps.model.Response
import yandex.cloud.sdk.functions.Context
import java.util.Base64

/**
 * Input:    /v1/project/create?
 * Output:  project/create
 */
fun String.dropVersionPrefix(versionPrefix: String) =
    "^\\/$versionPrefix\\/([^?]*)\\??\$".toRegex()
        .findAll(this)
        .firstOrNull()
        ?.groupValues
        ?.get(1)

val objectMapper: ObjectMapper = jacksonObjectMapper().findAndRegisterModules()

inline fun <reified T> Request.toTransportModel(): T =
    if (isBase64Encoded) {
        objectMapper.readValue(Base64.getDecoder().decode(body))
    } else {
        objectMapper.readValue(body!!)
    }

fun withProjectContext(context: Context, block: ProjectContext.() -> Response) =
    with(
        ProjectContext(
            timeStart = Clock.System.now(),
            requestId = RequestId(context.requestId),
        ),
    ) {
        block()
    }

fun withTaskContext(context: Context, block: TaskContext.() -> Response) =
    with(
        TaskContext(
            timeStart = Clock.System.now(),
            requestId = RequestId(context.requestId),
        ),
    ) {
        block()
    }

/**
 * V1 (jackson)
 */
fun me.neversleeps.api.jackson.v1.models.IResponse.toResponse(): Response =
    toResponse(objectMapper.writeValueAsString(this))

/**
 * V2 (multiplatform)
 */
fun me.neversleeps.api.multiplatform.v1.models.IResponse.toResponse(): Response =
    toResponse(apiMapper.encodeToString(this))

private fun toResponse(body: String): Response =
    Response(
        200,
        false,
        mapOf("Content-Type" to "application/json"),
        body,
    )
