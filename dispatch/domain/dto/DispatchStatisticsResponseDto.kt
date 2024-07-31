package com.ilogistic.delivery_admin_backend.dispatch.domain.dto

import com.querydsl.core.annotations.QueryProjection

data class DispatchStatisticsResponseDto @QueryProjection constructor(
    val totalDispatchCount: Long,
    val totalCompleteDispatchPrice: Int,
    val totalIncompleteDispatchPrice: Int,
) {
}
