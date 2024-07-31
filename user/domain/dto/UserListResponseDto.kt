package com.ilogistic.delivery_admin_backend.user.domain.dto

class UserListResponseDto(
    val id: Long,
    val username: String,
    val name: String,
    val role: String,
    val carNumber: String? = null,
    val companyName: String,
    val companyNumber: String,
    val point: Int,
) {
}
