package com.ilogistic.delivery_admin_backend.applicationfeepayment.domain.dto

import com.ilogistic.delivery_admin_backend.userpoint.enums.PointReason
import com.querydsl.core.annotations.QueryProjection
import java.time.LocalDateTime

data class ApplicationPaymentSelfResponseDto @QueryProjection constructor(
    val id: Long,
    val reason: PointReason,
    val amount: Int,
    val balance: Int,
    val createdDate: LocalDateTime
) {
}
