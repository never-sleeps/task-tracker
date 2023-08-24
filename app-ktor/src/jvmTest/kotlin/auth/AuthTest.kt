package auth

import io.ktor.client.request.* // ktlint-disable no-wildcard-imports
import io.ktor.server.testing.*
import me.neversleeps.app.base.KtorAuthConfig
import helpers.testSettings
import me.neversleeps.app.moduleJvm
import org.junit.Test
import kotlin.test.assertEquals

class AuthTest {
    @Test
    fun invalidAudience() = testApplication {
        application {
            moduleJvm(testSettings())
        }

        val response = client.post("/api/v1/project/create") {
            addAuth(config = KtorAuthConfig.TEST.copy(audience = "invalid"))
        }
        assertEquals(401, response.status.value)
    }
}
