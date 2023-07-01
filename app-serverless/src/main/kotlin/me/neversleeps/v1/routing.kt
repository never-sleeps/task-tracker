package me.neversleeps.v1 // ktlint-disable filename

import me.neversleeps.model.Request
import me.neversleeps.model.Response
import me.neversleeps.utils.dropVersionPrefix
import yandex.cloud.sdk.functions.Context

fun v1handlers(req: Request, reqContext: Context): Response =
    IV1HandleStrategy.strategiesByDiscriminator[req.url!!.dropVersionPrefix(IV1HandleStrategy.V1)]
        ?.handle(req, reqContext)
        ?: Response(400, false, emptyMap(), "Unknown path! Path: ${req.url}")
