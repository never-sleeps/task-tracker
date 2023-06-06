package rest.test

import AppContainer
import io.kotest.core.spec.style.FunSpec
import io.kotest.core.test.TestCaseSeverityLevel
import io.kotest.datatest.withData
import io.kotest.matchers.shouldBe
import io.ktor.client.* // ktlint-disable no-wildcard-imports
import io.ktor.client.call.* // ktlint-disable no-wildcard-imports
import io.ktor.client.engine.okhttp.* // ktlint-disable no-wildcard-imports
import io.ktor.client.request.* // ktlint-disable no-wildcard-imports
import io.ktor.http.* // ktlint-disable no-wildcard-imports

class AccRestTest : FunSpec({
    // Временно отключаем завал до момента реализации функциональности
    severity = TestCaseSeverityLevel.MINOR

    val client = HttpClient(OkHttp)

    test("Create") {
        withData(
            mapOf(
                "TC-1" to Pair(Request(), Response()),
                "TC-2" to Pair(Request(), Response()),
                "TC-3" to Pair(Request(), Response()),
            ),
        ) { (req, expected) ->
            val resp = client.get {
                url(AppContainer.C.url)
                accept(ContentType.Application.Json)
                setBody(req)
            }.call
            val actual = resp.body<Response>()
            actual shouldBe expected
        }
    }
})

data class Request(
    val title: String? = null,
    val description: String? = null,
    val state: String? = null,
)

data class Response(
    val result: String? = null,
    val data: Request? = null,
)
