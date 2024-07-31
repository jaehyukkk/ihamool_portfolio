package com.ilogistic.delivery_admin_backend.dispatchcomplete.domain.dto

import com.querydsl.core.annotations.QueryProjection

data class CompanyTotalStatisticsResponseDto @QueryProjection constructor(
    val count: Long,
    val totalAmount: Int,
    val totalAmountWithoutFee: Int,
) {
}
