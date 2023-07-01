package me.neversleeps

import me.neversleeps.model.Request
import me.neversleeps.model.Response
import me.neversleeps.v1.v1handlers
import me.neversleeps.v2.v2handlers
import yandex.cloud.sdk.functions.Context
import yandex.cloud.sdk.functions.YcFunction

@Suppress("unused")
class RoutingHandler : YcFunction<Request, Response> {

    override fun handle(event: Request, context: Context): Response =
        try {
            println(event)
            val validationResponse = validate(event)
            val url = event.url!!
            when {
                validationResponse != null -> validationResponse
                url.isVersion(me.neversleeps.v1.IV1HandleStrategy.V1) -> v1handlers(event, context)
                url.isVersion(me.neversleeps.v2.IV2HandleStrategy.V2) -> v2handlers(event, context)
                else -> Response(400, false, emptyMap(), "Unknown api version! Path: ${event.url}")
            }
        } catch (e: Exception) {
            Response(500, false, emptyMap(), "Unknown error: ${e.message}")
        }

    /**
     * Validate input event.
     */
    private fun validate(event: Request): Response? =
        when {
            event.httpMethod != "POST" -> Response(400, false, emptyMap(), "Invalid http method: ${event.httpMethod}")
            event.url == null -> Response(400, false, emptyMap(), "Invalid url")
            event.body == null -> Response(400, false, emptyMap(), "Invalid body")
            else -> null
        }

    private fun String.isVersion(versionPrefix: String): Boolean = startsWith("/$versionPrefix")
}
