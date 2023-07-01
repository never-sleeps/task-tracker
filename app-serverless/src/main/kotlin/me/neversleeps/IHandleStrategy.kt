package me.neversleeps

import me.neversleeps.model.Request
import me.neversleeps.model.Response
import yandex.cloud.sdk.functions.Context

interface IHandleStrategy {
    val version: String
    val path: String
    fun handle(req: Request, reqContext: Context): Response
}
