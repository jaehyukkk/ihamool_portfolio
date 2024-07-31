package com.ilogistic.delivery_admin_backend.dispatchcomplete.domain.dto

import com.querydsl.core.annotations.QueryProjection

data class DriverTotalStatisticsSelfResponseDto @QueryProjection constructor(
    val originalPrice: Int,
    val driverPrice: Int,
    val shipPrice: Int,
    val feePrice: Int,
    val count: Long,
){
}
