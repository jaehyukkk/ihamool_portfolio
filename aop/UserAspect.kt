package com.ilogistic.delivery_admin_backend.aop

import com.ilogistic.delivery_admin_backend.exception.BaseException
import com.ilogistic.delivery_admin_backend.exception.ErrorCode
import com.ilogistic.delivery_admin_backend.user.enums.UserRole
import org.aspectj.lang.JoinPoint
import org.aspectj.lang.annotation.Aspect
import org.aspectj.lang.annotation.Before
import org.aspectj.lang.reflect.MethodSignature
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component
import java.lang.reflect.Method

@Component
@Aspect
class UserAspect {
    @Before("@annotation(UserRights)")
    @Throws(Throwable::class)
    fun adminFranchiseeRights(joinPoint: JoinPoint) {
        val methodSignature = joinPoint.signature as MethodSignature
        val method: Method = methodSignature.method
        val userRights = method.getAnnotation(UserRights::class.java)
        val targetRoles: Array<UserRole> = userRights.targetRoles

        val authentication = SecurityContextHolder.getContext().authentication

        authentication.authorities.stream()
            .map { r: GrantedAuthority? -> r!!.authority}
            .toList()
            .any { userRole -> targetRoles.any { it.value == userRole } }
            .let {
                if (!it) {
                    throw BaseException(ErrorCode.FORBIDDEN)
                }
            }
    }
}
