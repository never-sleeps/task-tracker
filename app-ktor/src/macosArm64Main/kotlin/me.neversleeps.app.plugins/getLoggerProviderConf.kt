package me.neversleeps.app.plugins // ktlint-disable filename

import io.ktor.server.application.* // ktlint-disable no-wildcard-imports
import me.neversleeps.logging.common.LoggerProvider
import me.neversleeps.logging.kermit.mpLoggerKermit

actual fun Application.getLoggerProviderConf(): LoggerProvider = LoggerProvider {
    mpLoggerKermit(it)
}
