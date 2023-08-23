package me.neversleeps.app.config

import io.ktor.server.config.* // ktlint-disable no-wildcard-imports

data class GremlinConfig(
    val host: String = "localhost",
    val port: Int = 8182,
    val user: String = "root",
    val pass: String,
    val enableSsl: Boolean = false,
) {
    constructor(config: ApplicationConfig) : this(
        host = config.property("$PATH.host").getString(),
        user = config.property("$PATH.user").getString(),
        pass = config.property("$PATH.password").getString(),
        enableSsl = config.property("$PATH.enableSsl").getString().toBoolean(),
    )

    companion object {
        const val PATH = "${ConfigPaths.repository}.gremlin"
    }
}
