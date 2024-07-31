package com.ilogistic.delivery_admin_backend.applicationfeepayment.domain.dto

import com.ilogistic.delivery_admin_backend.user.enums.UserRole

class ApplicationFeePaymentSearchDto(
    val role: UserRole? = null,
    val startDate: String? = null,
    val endDate: String? = null,
    val searchWord: String? = null,
) {
}
