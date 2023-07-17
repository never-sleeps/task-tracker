package me.neversleeps.app.plugins // ktlint-disable filename

import io.ktor.server.application.* // ktlint-disable no-wildcard-imports
import me.neversleeps.logging.common.LoggerProvider
import me.neversleeps.logging.jvm.mpLoggerLogback
import me.neversleeps.logging.kermit.mpLoggerKermit

actual fun Application.getLoggerProviderConf(): LoggerProvider =
    when (val mode = environment.config.propertyOrNull("ktor.logger")?.getString()) {
        "kmp" -> LoggerProvider { mpLoggerKermit(it) }
        "logback", null -> LoggerProvider { mpLoggerLogback(it) }
        else -> throw Exception("Logger $mode is not allowed. Additted values are kmp and logback")
}
