package com.ilogistic.delivery_admin_backend.usersuspendstatus.domain.repository

import com.ilogistic.delivery_admin_backend.usersuspendstatus.domain.dto.UserSuspendStatusResponseDto

interface UserSuspendStatusRepositoryCustom {
    fun getSuspendLastInfo(userId: Long): UserSuspendStatusResponseDto?
}
