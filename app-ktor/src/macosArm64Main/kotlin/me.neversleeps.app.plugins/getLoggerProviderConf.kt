package me.neversleeps.app.plugins

import io.ktor.server.application.*
import me.neversleeps.logging.common.LoggerProvider
import ru.otus.otuskotlin.marketplace.logging.common.MpLoggerProvider
import ru.otus.otuskotlin.marketplace.logging.kermit.mpLoggerKermit

actual fun Application.getLoggerProviderConf(): LoggerProvider = MpLoggerProvider {
    mpLoggerKermit(it)
}
