package com.ilogistic.delivery_admin_backend.admingroup.domain.dto

class AdminGroupUserListResponseDto(
    val id: Long,
    val username: String,
    val name: String,
    val role: String,
    val carNumber: String? = null,
    val companyName: String,
    val companyNumber: String,
) {
}
