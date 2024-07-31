package com.ilogistic.delivery_admin_backend.message.domain.repository

import com.ilogistic.delivery_admin_backend.message.domain.entity.Message
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface MessageRepository : JpaRepository<Message, Long>{
    fun findByReceiverIdOrderByIdDesc(receiverId: Long): List<Message>

    fun findBySenderIdAndReceiverId(senderId: Long, receiverId: Long): List<Message>
}
