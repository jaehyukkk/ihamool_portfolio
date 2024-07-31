package com.ilogistic.delivery_admin_backend.message.service

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import com.ilogistic.delivery_admin_backend.dispatch.domain.dto.WebSocketSendDto
import com.ilogistic.delivery_admin_backend.dispatch.enums.WebSocketTopic
import com.ilogistic.delivery_admin_backend.handler.WebSocketHandler
import com.ilogistic.delivery_admin_backend.message.domain.dto.MessageRequestDto
import com.ilogistic.delivery_admin_backend.message.domain.dto.MessageResponseDto
import com.ilogistic.delivery_admin_backend.message.domain.entity.Message
import com.ilogistic.delivery_admin_backend.message.domain.repository.MessageRepository
import com.ilogistic.delivery_admin_backend.user.enums.UserRole
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Service

@Service
class MessageService(
    private val messageRepository: MessageRepository,
    private val webSocketHandler: WebSocketHandler
) {

    fun sendMessage(senderId: Long?, messageRequestDto: MessageRequestDto){

        val messageResponse = MessageResponseDto(
            message = messageRequestDto.message,
            senderName = messageRequestDto.senderName,
            senderId = senderId,
        )

        messageRequestDto.receiverIds!!.forEach { receiverId ->
            messageRequestDto.receiverId = receiverId
            messageRequestDto.senderId = senderId
            messageRepository.save(messageRequestDto.toEntity())
        }

        val channel = WebSocketSendDto.Companion.Channel(
            name = "default-1",
            targetIds = messageRequestDto.receiverIds!!.toMutableList()
        )

        val webSocketSendDto = WebSocketSendDto(
            data = messageResponse,
            topic = WebSocketTopic.MESSAGE_TOPIC,
            channels = mutableListOf(channel)
        )

        webSocketHandler.sendMessage(webSocketSendDto)
    }

    fun getUserMessage(userId: Long): List<Message> {
        return messageRepository.findByReceiverIdOrderByIdDesc(userId)
    }

    //특정 유저한테 보낸 메세지 가져오기
    fun getMessagesBySenderAndReceiverId(senderId: Long, receiverId: Long): List<Message> {
        return messageRepository.findBySenderIdAndReceiverId(senderId, receiverId)
    }

}
