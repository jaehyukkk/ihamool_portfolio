package com.ilogistic.delivery_admin_backend.dispatchtemplate.domain.dto

import com.ilogistic.delivery_admin_backend.dispatch.enums.DispatchDateValue
import com.ilogistic.delivery_admin_backend.dispatch.enums.DispatchStatus
import com.ilogistic.delivery_admin_backend.dispatch.enums.LoadingMethod
import com.ilogistic.delivery_admin_backend.dispatch.enums.PaymentType
import com.querydsl.core.annotations.QueryProjection

data class DispatchTemplateResponseDto @QueryProjection constructor(
    val id: Long? = null,
    val templateName: String,
    val startAddress: String? = null,
    val startDetailAddress: String? = null,
    val startZipCode: String? = null,
    val startSigungu: String? = null,
    val startSido: String? = null,

    val endAddress: String? = null,
    val endDetailAddress: String? = null,
    val endZipCode: String? = null,
    val endSigungu: String? = null,
    val endSido: String? = null,
    val originalPrice: Int? = null,
    val fee: Int? = null,
    val driverPrice: Int? = null,
    val loadingMethod: LoadingMethod? = null,
    val itemName: String? = null,
    val itemCount: Int? = null,
    val palletCount: Int? = null,
    val precautions: String? = null,
    val memo: String? = null,
    val dispatchStart: DispatchDateValue? = null,
    val dispatchEnd: DispatchDateValue? = null,
    val paymentType: PaymentType? = null,
    val franchiseeId: Long? = null,
    val franchiseeCompanyName: String? = null,
//    val franchiseeTel: String? = null,
    val managerPhone: String? = null,
    val franchiseeAddress: String? = null,
    val franchiseeDetailAddress: String? = null,

    val shipPrice: Int? = null,
    val addressType: String? = null,

    val carTypeIds: String? = null,
    val childCarTypeNames: String? = null,
    val parentCarTypeInfo: String? = null,

){
    var parentCarTypeName: String? = null
    var parentCarTypeId: Long? = null
    var ton: Double = 0.0

    init {
        //콘캣으로 들어온 값 분리
        parentCarTypeInfo?.let {
            val split = it.split(",")
            parentCarTypeName = split[0]
            parentCarTypeId = split[1].toLong()
            ton = split[2].toDouble()
        }
    }
}
