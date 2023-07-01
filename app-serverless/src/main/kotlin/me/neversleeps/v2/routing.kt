package me.neversleeps.v2 // ktlint-disable filename

import me.neversleeps.model.Request
import me.neversleeps.model.Response
import me.neversleeps.utils.dropVersionPrefix
import yandex.cloud.sdk.functions.Context

fun v2handlers(req: Request, reqContext: Context): Response =
    IV2HandleStrategy.strategiesByDiscriminator[req.url!!.dropVersionPrefix(IV2HandleStrategy.V2)]
        ?.handle(req, reqContext)
        ?: Response(400, false, emptyMap(), "Unknown path! Path: ${req.url}")
