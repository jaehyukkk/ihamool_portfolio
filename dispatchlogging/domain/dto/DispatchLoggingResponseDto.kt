package com.ilogistic.delivery_admin_backend.dispatchlogging.domain.dto

import com.ilogistic.delivery_admin_backend.dispatch.enums.DispatchStatus
import com.querydsl.core.annotations.QueryProjection
import java.time.LocalDateTime

data class DispatchLoggingResponseDto @QueryProjection constructor(
    val id : Long,
    val driverId: Long? = null,
    val driverName: String? = null,
    val driverPhone: String? = null,
    val driverCarNumber: String? = null,
    val status: DispatchStatus,
    val isForce: Boolean = false,
    val createdDate: LocalDateTime
) {
}
