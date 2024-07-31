package com.ilogistic.delivery_admin_backend.message.controller

import com.ilogistic.delivery_admin_backend.message.domain.dto.MessageRequestDto
import com.ilogistic.delivery_admin_backend.message.domain.entity.Message
import com.ilogistic.delivery_admin_backend.message.service.MessageService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.security.Principal

@RequestMapping("/api/v1/message")
@RestController
class MessageController(
    private val messageService: MessageService
) {

    @PostMapping()
    fun sendMessage(@RequestBody messageRequestDto: MessageRequestDto, principal: Principal) : ResponseEntity<Void> {
        messageService.sendMessage(principal.name.toLong(), messageRequestDto)
        return ResponseEntity.ok(null)
    }

    @GetMapping("/user")
    fun userMessage(principal: Principal) : ResponseEntity<List<Message>> {
        return ResponseEntity.ok(messageService.getUserMessage(principal.name.toLong()))
    }

    @GetMapping("/user/send/{receiverId}")
    fun userMessageBySenderAndReceiverId(principal: Principal, @PathVariable receiverId: Long) : ResponseEntity<List<Message>> {
        return ResponseEntity.ok(messageService.getMessagesBySenderAndReceiverId(principal.name.toLong(), receiverId))
    }
}
