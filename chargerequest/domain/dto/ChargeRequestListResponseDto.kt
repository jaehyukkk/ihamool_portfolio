package com.ilogistic.delivery_admin_backend.chargerequest.domain.dto

import com.ilogistic.delivery_admin_backend.chargerequest.enums.ChargeRequestStatus
import java.time.LocalDateTime

class ChargeRequestListResponseDto(
    val id : Long,
    val createdDate: LocalDateTime,
    val point: Int,
    val status: ChargeRequestStatus,

) {
}
