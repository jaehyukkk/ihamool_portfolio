package com.ilogistic.delivery_admin_backend.dispatchtemplate.domain.dto
import com.fasterxml.jackson.annotation.JsonIgnore
import com.ilogistic.delivery_admin_backend.dispatch.domain.entity.Dispatch
import com.ilogistic.delivery_admin_backend.dispatch.enums.DispatchDateValue
import com.ilogistic.delivery_admin_backend.dispatch.enums.LoadingMethod
import com.ilogistic.delivery_admin_backend.dispatch.enums.PaymentType
import com.ilogistic.delivery_admin_backend.dispatchtemplate.domain.entity.DispatchTemplate
import com.ilogistic.delivery_admin_backend.user.domain.entity.Driver
import com.ilogistic.delivery_admin_backend.user.domain.entity.Franchisee
import com.ilogistic.delivery_admin_backend.user.domain.entity.User

data class DispatchTemplateRequestDto(
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
    val paymentType: PaymentType? = null,
    val addressType: String? = null,
    var dispatchStart: DispatchDateValue? = null,
    var dispatchEnd: DispatchDateValue? = null,
    val franchiseeId: Long? = null,
    val shipPrice: Int? = null,
    @JsonIgnore
    var driver: Driver? = null,
    @JsonIgnore
    var franchisee: Franchisee? = null,

    val carTypeIds: List<Long>? = null
) {

    @JsonIgnore
    var id: Long? = null

//    init {
//        if (dispatchStartValue?.isNotEmpty() == true) {
//            dispatchStart = DispatchDateValue.fromValue(dispatchStartValue)
//        }
//        if (dispatchEndValue?.isNotEmpty() == true) {
//            dispatchEnd = DispatchDateValue.fromValue(dispatchEndValue)
//        }
//    }

    fun toEntity(user: User) : DispatchTemplate{
        return DispatchTemplate(
            id = id,
            templateName = templateName,
            startAddress = startAddress,
            startDetailAddress = startDetailAddress,
            startZipCode = startZipCode,
            startSigungu = startSigungu,
            startSido = startSido,
            endAddress = endAddress,
            endDetailAddress = endDetailAddress,
            endZipCode = endZipCode,
            endSigungu = endSigungu,
            endSido = endSido,
            originalPrice = originalPrice,
            fee = fee,
            driverPrice = driverPrice,
            loadingMethod = loadingMethod,
            itemName = itemName,
            itemCount = itemCount,
            palletCount = palletCount,
            precautions = precautions,
            memo = memo,
            dispatchStart = dispatchStart,
            dispatchEnd = dispatchEnd,
            paymentType = paymentType,
            addressType = addressType,
            shipPrice = shipPrice,
            franchisee = franchisee,
            user = user
        )
    }
}
