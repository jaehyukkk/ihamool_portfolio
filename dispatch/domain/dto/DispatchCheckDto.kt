package com.ilogistic.delivery_admin_backend.dispatch.domain.dto

import com.ilogistic.delivery_admin_backend.dispatch.enums.DispatchStatus
import com.querydsl.core.annotations.QueryProjection
import java.time.LocalDateTime

data class DispatchCheckDto @QueryProjection constructor(
    val status : DispatchStatus,
    val dispatchDateTime: LocalDateTime,
    val driverPrice: Int,
) {
}
