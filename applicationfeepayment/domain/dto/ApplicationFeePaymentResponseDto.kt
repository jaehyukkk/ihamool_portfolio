package com.ilogistic.delivery_admin_backend.applicationfeepayment.domain.dto

import com.querydsl.core.annotations.QueryProjection
import java.time.LocalDateTime

data class ApplicationFeePaymentResponseDto @QueryProjection constructor(
    val id: Long,
    val userId: Long,
    val name: String,
    val role: String,
    val amount: Int,
    val balance: Int,
    val createdDate: LocalDateTime
) {
}
