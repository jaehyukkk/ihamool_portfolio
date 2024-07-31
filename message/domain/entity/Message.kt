package com.ilogistic.delivery_admin_backend.message.domain.entity

import com.ilogistic.delivery_admin_backend.entity.BaseEntity
import com.ilogistic.delivery_admin_backend.message.enums.MessageType
import com.ilogistic.delivery_admin_backend.user.enums.UserRole
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id

@Entity
class Message(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,
    @Column(nullable = false, length = 1000)
    val message: String,
    val senderName: String? = null,
    val senderId: Long? = null,
    val receiverId: Long? = null,
    val messageType: MessageType = MessageType.CHAT
) : BaseEntity(){
}

