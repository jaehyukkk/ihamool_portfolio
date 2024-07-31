package com.ilogistic.delivery_admin_backend.config

import com.ilogistic.delivery_admin_backend.handler.WebSocketHandler
import org.springframework.context.annotation.Configuration
import org.springframework.web.socket.config.annotation.*

@Configuration
@EnableWebSocket
class WebSocketConfig(
    private val webSocketHandler: WebSocketHandler
) : WebSocketConfigurer {

    override fun registerWebSocketHandlers(registry: WebSocketHandlerRegistry) {
        registry.addHandler(webSocketHandler, "/api-websocket/websocket").setAllowedOrigins("*")
    }

}
