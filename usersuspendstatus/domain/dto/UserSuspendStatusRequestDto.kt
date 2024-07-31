package com.ilogistic.delivery_admin_backend.usersuspendstatus.domain.dto

import com.ilogistic.delivery_admin_backend.user.domain.entity.User
import com.ilogistic.delivery_admin_backend.usersuspendstatus.domain.entity.UserSuspendStatus
import com.ilogistic.delivery_admin_backend.usersuspendstatus.enums.SuspendType

class UserSuspendStatusRequestDto(
    var userId: Long? = null,
    var handlerUserId: Long? = null,
    var user: User? = null,
    var status: Boolean? = true,
    var suspendType: SuspendType,
    var reason: String,
) {

    fun toEntity(user: User) : UserSuspendStatus{
        return UserSuspendStatus(
            user = user,
            status = status,
            reason = reason,
            suspendType = suspendType,
        )
    }
}
