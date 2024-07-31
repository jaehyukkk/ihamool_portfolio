package com.ilogistic.delivery_admin_backend.message.domain.dto

import com.fasterxml.jackson.annotation.JsonIgnore
import com.ilogistic.delivery_admin_backend.message.domain.entity.Message
import com.ilogistic.delivery_admin_backend.message.enums.MessageType
import com.ilogistic.delivery_admin_backend.user.enums.UserRole

class MessageRequestDto(
    val message: String,
    val senderName: String,
    @JsonIgnore
    var receiverId: Long? = null,
    var receiverIds: List<Long>? = null,
    var messageType: MessageType = MessageType.CHAT,
) {
    var senderId: Long? = null
    fun toEntity() : Message {
        return Message(
            message = message,
            senderName = senderName,
            senderId = senderId,
            receiverId = receiverId,
            messageType = messageType
        )
    }
}
