package com.ilogistic.delivery_admin_backend.user.domain.dto

import com.ilogistic.delivery_admin_backend.user.domain.entity.Admin

class AdminResponseDto(
    val id: Long,
    val username: String,
) {

    companion object {
        fun of(admin: Admin, username: String): AdminResponseDto {
            return AdminResponseDto(
                id = admin.id!!,
                username = username,
            )
        }
    }
}
