package com.ilogistic.delivery_admin_backend.usersuspendstatus.domain.dto

import com.ilogistic.delivery_admin_backend.usersuspendstatus.enums.SuspendType
import com.querydsl.core.annotations.QueryProjection

data class UserSuspendStatusResponseDto @QueryProjection constructor(
    val reason: String,
    val suspendType: SuspendType,
) {
}
