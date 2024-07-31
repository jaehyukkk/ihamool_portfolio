package com.ilogistic.delivery_admin_backend.message.domain.dto

import java.time.LocalDateTime

class MessageResponseDto(
    val id: Long? = null,
    val message: String,
    val senderName: String,
    val senderId: Long? = null,
    val createdDate: LocalDateTime? = null,
) {
}
