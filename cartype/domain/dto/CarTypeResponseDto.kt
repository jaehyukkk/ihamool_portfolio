package com.ilogistic.delivery_admin_backend.cartype.domain.dto

import com.querydsl.core.annotations.QueryProjection
import java.time.LocalDateTime

data class CarTypeResponseDto @QueryProjection constructor(
    val id: Long,
    val name: String,
    val description: String,
    val parentId: Long? = null,
    val parentName: String? = null,
    val ton: Double,
    val createdDate: LocalDateTime,
) {
}
