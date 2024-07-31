package com.ilogistic.delivery_admin_backend.applicationfeepayment.domain.dto

import com.querydsl.core.annotations.QueryProjection

data class ApplicationFeePaymentStatisticsDayResponseDto @QueryProjection constructor(
//    val driverTotalCount: Long,
//    val driverTotalAmount: Int,
//    val franchiseeTotalCount: Long,
//    val franchiseeTotalAmount: Int,
    val totalCount: Long,
    val totalAmount: Int,
    val date: String
) {
}
