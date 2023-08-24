import helpers.testSettings // ktlint-disable no-wildcard-imports
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.server.testing.*
import me.neversleeps.app.module
import kotlin.test.Test
import kotlin.test.assertEquals

class GeneralApplicationTest {
    @Test
    fun `root endpoint`() = testApplication {
        application { module(testSettings()) }
        val response = client.get("/")
        assertEquals(HttpStatusCode.OK, response.status)
    }
}
