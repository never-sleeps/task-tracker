package me.neversleeps.appspring.v1.ws

import org.springframework.context.annotation.Configuration
import org.springframework.web.socket.config.annotation.EnableWebSocket
import org.springframework.web.socket.config.annotation.WebSocketConfigurer
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry

@Configuration
@EnableWebSocket
class WebSocketConfigV1(
    val projectHandlerV1: WsProjectHandlerV1,
    val taskHandlerV1: WsProjectHandlerV1,
) : WebSocketConfigurer {

    override fun registerWebSocketHandlers(registry: WebSocketHandlerRegistry) {
        registry.addHandler(projectHandlerV1, "/ws/v1/project").setAllowedOrigins("*")
        registry.addHandler(taskHandlerV1, "/ws/v1/task").setAllowedOrigins("*")
    }
}
