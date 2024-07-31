package com.ilogistic.delivery_admin_backend.aop

import com.ilogistic.delivery_admin_backend.exception.BaseException
import com.ilogistic.delivery_admin_backend.exception.CustomMessageRuntimeException
import com.ilogistic.delivery_admin_backend.exception.ErrorCode
import com.ilogistic.delivery_admin_backend.user.enums.UserRole
import com.ilogistic.delivery_admin_backend.usersuspendstatus.service.UserSuspendStatusService
import org.aspectj.lang.annotation.Aspect
import org.aspectj.lang.annotation.Before
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component

@Component
@Aspect
class SuspendAspect(
    private val userSuspendStatusService: UserSuspendStatusService
) {

    @Before("@annotation(SuspendCheck)")
    @Throws(Throwable::class)
    fun suspendCheck() {

        val authentication = SecurityContextHolder.getContext().authentication
        val suspendTargetRole = authentication.authorities.stream()
            .anyMatch { r: GrantedAuthority? -> (r!!.authority == UserRole.FRANCHISEE.value || r.authority == UserRole.DRIVER.value)  }

        if (suspendTargetRole) {
            val userId = authentication.name.toLong()
            val userSuspendStatusResponseDto = userSuspendStatusService.getSuspendLastInfo(userId)
            if (userSuspendStatusResponseDto != null) {
                throw CustomMessageRuntimeException(ErrorCode.USER_SUSPEND, userSuspendStatusResponseDto.reason)
            }
        }
    }
}
