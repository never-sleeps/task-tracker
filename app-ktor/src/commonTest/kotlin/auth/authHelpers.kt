package auth

import io.ktor.client.request.*
import me.neversleeps.app.base.KtorAuthConfig

expect fun HttpRequestBuilder.addAuth(
    id: String = "user1",
    groups: List<String> = listOf("USER", "TEST"),
    config: KtorAuthConfig = KtorAuthConfig.TEST,
)
