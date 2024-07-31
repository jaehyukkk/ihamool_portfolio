package com.ilogistic.delivery_admin_backend.applicationfeepayment.domain.dto

import com.ilogistic.delivery_admin_backend.user.enums.UserRole

class ApplicationFeePaymentStatisticsDaySearchDto(
    val startDate: String? = null,
    val endDate: String? = null,
    val role: UserRole? = null,
) {
}
