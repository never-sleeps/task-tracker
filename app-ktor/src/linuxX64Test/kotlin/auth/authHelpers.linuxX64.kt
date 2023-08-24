package auth

import io.ktor.client.request.*
import me.neversleeps.app.base.KtorAuthConfig

actual fun HttpRequestBuilder.addAuth(
    id: String,
    groups: List<String>,
    config: KtorAuthConfig
) {
}
