package com.ilogistic.delivery_admin_backend.chargerequest.domain.dto

import com.ilogistic.delivery_admin_backend.chargerequest.enums.ChargeRequestStatus
import com.querydsl.core.annotations.QueryProjection
import java.time.LocalDateTime

data class ChargeRequestAllUserListResponseDto @QueryProjection constructor(
    val id : Long,
    val createdDate: LocalDateTime,
    val point: Int,
    val status: ChargeRequestStatus,
    val userId: Long,
    val name: String,
    val username: String,
    val role: String,
) {
}
