package com.ilogistic.delivery_admin_backend.dispatchcomplete.domain.dto

import com.querydsl.core.annotations.QueryProjection
import java.time.LocalDateTime

data class DriverCompleteScreenListResponseDto @QueryProjection constructor(
    val id: Long,
    val dispatchId: Long,
    val originalPrice: Int,
    val deliveryPrice: Int,
    val createdDate: LocalDateTime,
    val loadingDate: LocalDateTime,
    val dispatchDate: LocalDateTime,
    val address: String,
    val detailAddress: String? = null,
    val distance: Double,
    val isTaxInvoice: Boolean
) {
}
