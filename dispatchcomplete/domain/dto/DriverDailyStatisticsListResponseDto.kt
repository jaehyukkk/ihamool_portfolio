package com.ilogistic.delivery_admin_backend.dispatchcomplete.domain.dto

import com.querydsl.core.annotations.QueryProjection
import java.time.LocalDateTime

data class DriverDailyStatisticsListResponseDto @QueryProjection constructor(
    val id: Long,
    val dispatchId: Long,
    val startSigungu: String,
    val startSido: String,
    val endSigungu: String,
    val endSido: String,
    val distance: Double,
    val fee: Int,
    val originalPrice: Int,
    val feePrice: Int,
    val driverPrice: Int,
    val shipPrice: Int,
    val createdDate: LocalDateTime
) {
}
