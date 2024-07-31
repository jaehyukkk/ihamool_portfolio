package com.ilogistic.delivery_admin_backend.dispatch.domain.dto

import com.ilogistic.delivery_admin_backend.dispatch.enums.DispatchStatus
import com.ilogistic.delivery_admin_backend.dispatch.enums.LoadingMethod
import com.ilogistic.delivery_admin_backend.dispatch.enums.PaymentType
import com.querydsl.core.annotations.QueryProjection
import java.time.LocalDateTime

data class DispatchPageListResponseDto @QueryProjection constructor(
    val id : Long,
    val startAddress: String,
    val endAddress: String,
    val itemName: String,
    val paymentType: PaymentType,
    val originalPrice: Int,
    val fee: Int,
    val driverPrice: Int,
    val loadingMethod: LoadingMethod,
    val driverId: Long? = null,
    val driverName: String? = null,
    val driverPhone: String? = null,
    val driverCarNumber: String? = null,
    val shipPrice: Int? = null,
    val status: DispatchStatus,
    val distance: Double,
    val weight: Double,
    val createdDate: LocalDateTime
) {
}
