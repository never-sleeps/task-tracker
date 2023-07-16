package me.neversleeps.app.plugins

import io.ktor.server.application.* // ktlint-disable no-wildcard-imports
import me.neversleeps.app.AppSettings
import me.neversleeps.business.ProjectProcessor
import me.neversleeps.business.TaskProcessor
import me.neversleeps.common.CorSettings
import me.neversleeps.logging.common.LoggerProvider

fun Application.initAppSettings(): AppSettings = AppSettings(
    appUrls = environment.config.propertyOrNull("ktor.urls")?.getList() ?: emptyList(),
    corSettings = CorSettings(
        loggerProvider = getLoggerProviderConf(),
    ),
    projectProcessor = ProjectProcessor(),
    taskProcessor = TaskProcessor(),
)

expect fun Application.getLoggerProviderConf(): LoggerProvider
