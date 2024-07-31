package com.ilogistic.delivery_admin_backend.dispatch.domain.dto

import com.ilogistic.delivery_admin_backend.cartype.domain.dto.CarTypeBriefResponseDto
import com.ilogistic.delivery_admin_backend.cartype.domain.dto.CarTypeResponseDto
import com.ilogistic.delivery_admin_backend.cartype.domain.entity.CarType
import com.ilogistic.delivery_admin_backend.dispatch.enums.LoadingMethod
import com.ilogistic.delivery_admin_backend.dispatch.enums.PaymentType
import com.querydsl.core.annotations.QueryProjection
import java.time.LocalDateTime

data class DispatchRequestResponseDto @QueryProjection constructor(
    val id : Long,
    val startAddress : String,
    val endAddress : String,
    val itemName: String,
    val itemCount: Int,
    val palletCount: Int,
    val paymentType: PaymentType,
    val originalPrice: Int,
    val franchiseeCompanyName: String,
    val franchiseeManagerPhone: String,
    val loadingMethod: LoadingMethod,
    val addressType: String,
    val createdDate: LocalDateTime,
    val carTypes: String,
    ) {
}
