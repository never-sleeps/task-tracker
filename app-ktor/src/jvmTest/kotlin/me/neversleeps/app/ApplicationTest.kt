package me.neversleeps.app

import io.ktor.client.request.* // ktlint-disable no-wildcard-imports
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.server.testing.*
import org.junit.Test
import kotlin.test.assertEquals

class ApplicationTest {
    @Test
    fun `root endpoint`() = testApplication {
        val response = client.get("/")
        assertEquals(HttpStatusCode.OK, response.status)
        assertEquals("Hello, world!", response.bodyAsText())
    }
}
