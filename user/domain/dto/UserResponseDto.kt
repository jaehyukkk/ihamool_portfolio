package com.ilogistic.delivery_admin_backend.user.domain.dto

import com.ilogistic.delivery_admin_backend.user.domain.entity.User
import java.time.LocalDateTime

class UserResponseDto(
    var id: Long? = null,
    var username: String,
    var userRole: String,
    var createdDate: LocalDateTime? = null,
    var updatedDate: LocalDateTime? = null,
    var roles : List<String>,
    val detailInfo: Any? = null
) {

    companion object{
        fun of(user : User) : UserResponseDto {
            return UserResponseDto(
                id = user.id,
                username = user.username,
                userRole = getAuthorities(user.userRole)[0],
                roles = getAuthorities(user.userRole),
                createdDate = user.createdDate,
                updatedDate = user.updatedDate,
            )
        }

        private fun getAuthorities(userRole: String): List<String> {
            return userRole.split(",")
        }
    }
}
