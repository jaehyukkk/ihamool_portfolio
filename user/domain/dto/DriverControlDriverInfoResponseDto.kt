package com.ilogistic.delivery_admin_backend.user.domain.dto

import com.querydsl.core.annotations.QueryProjection

data class DriverControlDriverInfoResponseDto @QueryProjection constructor(
    val id: Long,
    val name: String,
    val phone: String,
    val carNumber: String,
    val dispatchCount: Long,
    val loadingCount: Long
) {
}
