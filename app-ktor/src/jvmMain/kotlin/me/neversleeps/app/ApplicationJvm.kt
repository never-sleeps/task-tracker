package me.neversleeps.app

import com.auth0.jwt.JWT
import io.ktor.http.* // ktlint-disable no-wildcard-imports
import io.ktor.serialization.jackson.jackson
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.http.content.resources
import io.ktor.server.http.content.static
import io.ktor.server.plugins.contentnegotiation.ContentNegotiation
import io.ktor.server.routing.*
import io.ktor.server.websocket.*
import io.ktor.util.*
import me.neversleeps.api.jackson.apiMapper
import me.neversleeps.app.base.KtorAuthConfig.Companion.GROUPS_CLAIM
import me.neversleeps.app.base.resolveAlgorithm
import me.neversleeps.app.jackson.WsProjectControllerV1
import me.neversleeps.app.jackson.WsTaskControllerV1
import me.neversleeps.app.jackson.project
import me.neversleeps.app.jackson.task
import me.neversleeps.app.plugins.initAppSettings
import me.neversleeps.app.simpleWS.wsChat
import me.neversleeps.app.simpleWS.wsPing
import me.neversleeps.app.module as commonModule

// function with config (application.conf)
fun main(args: Array<String>): Unit = io.ktor.server.cio.EngineMain.main(args)

private val clazz = Application::moduleJvm::class.qualifiedName ?: "Application"

@Suppress("unused") // Referenced in application.conf
fun Application.moduleJvm(appSettings: AppSettings = initAppSettings()) {
    commonModule(appSettings)

    val wsProjectHandler = WsProjectControllerV1()
    val wsTaskHandler = WsTaskControllerV1()

    install(Authentication) {
        jwt("auth-jwt") {
            val authConfig = appSettings.auth
            realm = authConfig.realm

            verifier {
                val algorithm = it.resolveAlgorithm(authConfig)
                JWT
                    .require(algorithm)
                    .withAudience(authConfig.audience)
                    .withIssuer(authConfig.issuer)
                    .build()
            }
            validate { jwtCredential: JWTCredential ->
                when {
                    jwtCredential.payload.getClaim(GROUPS_CLAIM).asList(String::class.java).isNullOrEmpty() -> {
                        this@moduleJvm.log.error("Groups claim must not be empty in JWT token")
                        null
                    }

                    else -> JWTPrincipal(jwtCredential.payload)
                }
            }
        }
    }
    routing {
        route("api/v1") {
            pluginRegistry.getOrNull(AttributeKey("ContentNegotiation")) ?: install(ContentNegotiation) {
                jackson {
                    setConfig(apiMapper.serializationConfig)
                    setConfig(apiMapper.deserializationConfig)
                }
            }

            authenticate("auth-jwt") {
                project(appSettings)
                task(appSettings)
            }
        }

        webSocket("/ws/ping") {
            wsPing()
        }
        webSocket("/ws/chat") {
            wsChat()
        }

        webSocket("/ws/v1/project") {
            wsProjectHandler.handle(this, appSettings)
        }
        webSocket("/ws/v1/task") {
            wsTaskHandler.handle(this, appSettings)
        }

        static("static") {
            resources("static")
        }
    }
}
