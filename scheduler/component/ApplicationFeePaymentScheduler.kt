package com.ilogistic.delivery_admin_backend.scheduler.component

import com.ilogistic.delivery_admin_backend.applicationfeepayment.service.ApplicationFeePaymentService
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component


@Component
class ApplicationFeePaymentScheduler(
    private val applicationFeePaymentService: ApplicationFeePaymentService,
//    private val userService: UserService,
//    private val userSuspendStatusService: UserSuspendStatusService
) {

//    @Scheduled(cron = "*/30 * * * * *")
//    @Scheduled(cron = "0 0 2 * * *", zone = "Asia/Seoul")
    @Scheduled(cron = "0 0 0 1 * *", zone = "Asia/Seoul")
    fun applicationFeePaymentScheduler() {
        applicationFeePaymentService.paymentProcedure()
    }
}
