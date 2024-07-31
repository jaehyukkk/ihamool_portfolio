package com.ilogistic.delivery_admin_backend.dispatchcomplete.domain.dto

import com.querydsl.core.annotations.QueryProjection

data class DriverSelfStatisticsListResponseDto @QueryProjection constructor(
    val count: Long,
    val driverPrice: Int,
    val date: String,
) {
}
