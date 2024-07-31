package com.ilogistic.delivery_admin_backend.aop

import com.ilogistic.delivery_admin_backend.applicationfeepayment.service.ApplicationFeePaymentService
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
class PaymentAspect(
//    private val userSuspendStatusService: UserSuspendStatusService
    private val applicationFeePaymentService: ApplicationFeePaymentService
) {

    @Before("@annotation(PaymentCheck)")
    @Throws(Throwable::class)
    fun paymentCheck() {

        val authentication = SecurityContextHolder.getContext().authentication
        val paymentTargetRole = authentication.authorities.stream()
            .anyMatch { r: GrantedAuthority? -> (r!!.authority == UserRole.FRANCHISEE.value || r.authority == UserRole.DRIVER.value)  }

//        if (paymentTargetRole) {
//            val userId = authentication.name.toLong()
//            val isPayment : Boolean = applicationFeePaymentService.getPaymentCheck(userId)
//            if (!isPayment) {
//                throw CustomMessageRuntimeException(ErrorCode.USER_SUSPEND, "어플리케이션 사용료를 납부하지 않았습니다.")
//            }
//        }
    }
}
