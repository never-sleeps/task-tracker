package me.neversleeps.rabbitmq.config

import me.neversleeps.common.CorSettings
import me.neversleeps.logging.common.LoggerProvider
import me.neversleeps.logging.jvm.mpLoggerLogback

private val loggerProvider = LoggerProvider { mpLoggerLogback(it) }

val corSettings = CorSettings(
    loggerProvider = loggerProvider,
)
