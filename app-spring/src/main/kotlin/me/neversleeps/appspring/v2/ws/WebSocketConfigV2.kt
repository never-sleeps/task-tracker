package me.neversleeps.appspring.v2.ws

import org.springframework.context.annotation.Configuration
import org.springframework.web.socket.config.annotation.EnableWebSocket
import org.springframework.web.socket.config.annotation.WebSocketConfigurer
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry

@Configuration
@EnableWebSocket
class WebSocketConfigV2(
    val projectHandlerV2: WsProjectHandlerV2,
    val taskHandlerV2: WsProjectHandlerV2,
) : WebSocketConfigurer {

    override fun registerWebSocketHandlers(registry: WebSocketHandlerRegistry) {
        registry.addHandler(projectHandlerV2, "/ws/v2/project").setAllowedOrigins("*")
        registry.addHandler(taskHandlerV2, "/ws/v2/task").setAllowedOrigins("*")
    }
}
