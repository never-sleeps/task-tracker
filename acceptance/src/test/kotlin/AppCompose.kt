package me.neversleeps.acceptance

import org.testcontainers.containers.DockerComposeContainer
import org.testcontainers.containers.wait.strategy.Wait // ktlint-disable no-unused-imports
import java.io.File
import java.time.Duration // ktlint-disable no-unused-imports

@Suppress("unused")
class AppCompose private constructor() {
    private val _service = "app_1"
    private val _port = 80

    private val compose =
        DockerComposeContainer(File("../_deploy/docker-compose-app.yml")).apply {
            withExposedService(
                _service,
                _port,
//                Wait.forListeningPort().withStartupTimeout(Duration.ofSeconds(30))
            )
            withLocalCompose(true)
            start()
        }

    val hostApp: String = compose.getServiceHost(_service, _port)
    val portApp: Int = compose.getServicePort(_service, _port)

    fun close() {
        compose.close()
    }

    companion object {
        val C by lazy { AppCompose() }
    }
}
