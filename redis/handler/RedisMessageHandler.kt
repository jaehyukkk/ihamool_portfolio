package com.ilogistic.delivery_admin_backend.redis.handler

import com.ilogistic.delivery_admin_backend.dispatch.domain.dto.WebSocketSendDto
import com.ilogistic.delivery_admin_backend.handler.WebSocketHandler
import org.springframework.data.redis.connection.Message
import org.springframework.data.redis.connection.MessageListener
import org.springframework.web.socket.TextMessage
import org.springframework.web.socket.WebSocketMessage
import org.springframework.web.socket.WebSocketSession


class RedisMessageHandler(
    private val session: WebSocketSession,
) : MessageListener {

    override fun onMessage(message: Message, pattern: ByteArray?) {
        val gson = com.google.gson.Gson()
        val payload = String(message.body)
        val webSocketSendDto : WebSocketSendDto = gson.fromJson(payload, WebSocketSendDto::class.java)
        if (session.isOpen) {
            if (webSocketSendDto.targetIds.isNullOrEmpty()) {
                println("send message to all")
            } else {
                println("send message to targetIds")
                println(payload)
            }

            if(!webSocketSendDto.targetIds.isNullOrEmpty()){
                if (webSocketSendDto.targetIds!!.contains(session.attributes["userId"])) {
                    session.sendMessage(TextMessage(payload))
                }
            } else {
                session.sendMessage(TextMessage(payload))
            }
        }
//        val webSocketMessage: WebSocketMessage<String> = TextMessage(payload)
//        session.sendMessage(webSocketMessage)
    }
}
