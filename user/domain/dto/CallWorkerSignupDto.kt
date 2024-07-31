package com.ilogistic.delivery_admin_backend.user.domain.dto

import com.ilogistic.delivery_admin_backend.user.domain.entity.CallWorker

class CallWorkerSignupDto(
    val username: String,
    val password: String,
    val name: String,
    val phone: String,
    val number: Int,
) {

    fun toEntity(id: Long) : CallWorker{
        return CallWorker(
            id = id,
            name = name,
            phone = phone,
            number = number,
        )
    }
}
