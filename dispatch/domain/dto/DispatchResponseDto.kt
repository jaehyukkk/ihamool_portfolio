package com.ilogistic.delivery_admin_backend.dispatch.domain.dto

import com.ilogistic.delivery_admin_backend.dispatch.domain.entity.Dispatch
import com.ilogistic.delivery_admin_backend.dispatch.enums.DispatchDateValue
import com.ilogistic.delivery_admin_backend.dispatch.enums.DispatchStatus
import com.ilogistic.delivery_admin_backend.dispatch.enums.LoadingMethod
import com.ilogistic.delivery_admin_backend.dispatch.enums.PaymentType
import com.querydsl.core.annotations.QueryProjection
import java.time.LocalDateTime

class DispatchResponseDto @QueryProjection constructor(
    val id: Long,
    val startAddress: String,
    val startDetailAddress: String? = null,
    val startZipCode: String? = null,
    val startSigungu: String,
    val startSido: String,
    var startLatitude: Double? = null,
    var startLongitude: Double? = null,

    val endAddress: String,
    val endDetailAddress: String? = null,
    val endZipCode: String? = null,
    val endSigungu: String,
    val endSido: String,
    var endLatitude: Double? = null,
    var endLongitude: Double? = null,

    val viaAddress: String? = null,
    val viaDetailAddress: String? = null,
    val viaZipCode: String? = null,
    val viaSigungu: String? = null,
    val viaSido: String? = null,
    var viaLatitude: Double? = null,
    var viaLongitude: Double? = null,

    var distance: Double,
    val originalPrice: Int,
    val fee: Int,
    val driverPrice: Int,
    val loadingMethod: LoadingMethod,
    val itemName: String,
    val itemCount: Int,
    val palletCount: Int,
    val weight: Double,
    val precautions: String? = null,
    val memo: String? = null,
    val dispatchStart: DispatchDateValue,
    val dispatchEnd: DispatchDateValue,
    val status: DispatchStatus,
    val paymentType: PaymentType,
    val franchiseeId: Long,
    val franchiseeCompanyName: String,
    val franchiseeManagerPhone: String,
    val franchiseeAddress: String,
    val franchiseeDetailAddress: String,

    val shipPrice: Int,
    val dispatchDateTime: LocalDateTime? = null,
    val completeDateTime: LocalDateTime? = null,

    val addressType: String,

    val dispatchCode: String

//    val carTypeList : Set<CarTypeResponseDto> ? = null

) {
}
