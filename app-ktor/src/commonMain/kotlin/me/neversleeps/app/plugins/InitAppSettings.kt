package me.neversleeps.app.plugins

import io.ktor.server.application.* // ktlint-disable no-wildcard-imports
import me.neversleeps.app.AppSettings
import me.neversleeps.app.base.KtorAuthConfig
import me.neversleeps.business.ProjectProcessor
import me.neversleeps.business.TaskProcessor
import me.neversleeps.common.CorSettings
import me.neversleeps.logging.common.LoggerProvider

fun Application.initAppSettings(): AppSettings {
    val corSettings = CorSettings(
        loggerProvider = getLoggerProviderConf(),
    )
    return AppSettings(
        appUrls = environment.config.propertyOrNull("ktor.urls")?.getList() ?: emptyList(),
        corSettings = corSettings,
        projectProcessor = ProjectProcessor(corSettings),
        taskProcessor = TaskProcessor(corSettings),
        auth = initAppAuth(),
    )
}

expect fun Application.getLoggerProviderConf(): LoggerProvider

private fun Application.initAppAuth(): KtorAuthConfig = KtorAuthConfig(
    secret = environment.config.propertyOrNull("jwt.secret")?.getString() ?: "",
    issuer = environment.config.property("jwt.issuer").getString(),
    audience = environment.config.property("jwt.audience").getString(),
    realm = environment.config.property("jwt.realm").getString(),
    clientId = environment.config.property("jwt.clientId").getString(),
    certUrl = environment.config.propertyOrNull("jwt.certUrl")?.getString(),
)
