package com.ilogistic.delivery_admin_backend.dispatch.domain.dto

import com.fasterxml.jackson.annotation.JsonIgnore
import com.ilogistic.delivery_admin_backend.admingroup.domain.entity.AdminGroup
import com.ilogistic.delivery_admin_backend.cartype.domain.entity.CarType
import com.ilogistic.delivery_admin_backend.dispatch.domain.entity.Dispatch
import com.ilogistic.delivery_admin_backend.dispatch.enums.DispatchDateValue
import com.ilogistic.delivery_admin_backend.dispatch.enums.DispatchStatus
import com.ilogistic.delivery_admin_backend.dispatch.enums.LoadingMethod
import com.ilogistic.delivery_admin_backend.dispatch.enums.PaymentType
import com.ilogistic.delivery_admin_backend.user.domain.entity.Admin
import com.ilogistic.delivery_admin_backend.user.domain.entity.Driver
import com.ilogistic.delivery_admin_backend.user.domain.entity.Franchisee
import com.ilogistic.delivery_admin_backend.user.domain.entity.User
import java.time.LocalDateTime

data class DispatchRequestDto(
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
    val precautions: String? = null,
    val memo: String? = null,
    val paymentType: PaymentType,
    val addressType: String,
    var dispatchStart: DispatchDateValue,
    var dispatchEnd: DispatchDateValue,
    var franchiseeId: Long ? = null,
    val shipPrice: Int = 0,
    @JsonIgnore
    var driver: Driver? = null,
    @JsonIgnore
    var franchisee: Franchisee? = null,
    @JsonIgnore
    var finalApprovalUser: User? = null,
    val carTypeIds: List<Long>,
    val parentCarTypeId: Long,
    val ton: Double = 0.0,

    val forceDriverId: Long? = null

) {

    @JsonIgnore
    var id: Long? = null
    @JsonIgnore
    var status: DispatchStatus? = DispatchStatus.WAITING
    @JsonIgnore
    var user : User? = null
    @JsonIgnore
    var parentCarType: CarType? = null
    @JsonIgnore
    var dispatchDateTime: LocalDateTime? = null
    @JsonIgnore
    var adminGroup: AdminGroup? = null

    fun toEntity(dispatchCode : String) : Dispatch{
        return Dispatch(
            id = id,
            startAddress = startAddress,
            startDetailAddress = startDetailAddress,
            startZipCode = startZipCode,
            startSigungu = startSigungu,
            startSido = startSido,
            startLatitude = startLatitude!!,
            startLongitude = startLongitude!!,
            endAddress = endAddress,
            endDetailAddress = endDetailAddress,
            endZipCode = endZipCode,
            endSigungu = endSigungu,
            endSido = endSido,
            endLatitude = endLatitude!!,
            endLongitude = endLongitude!!,
            viaAddress = viaAddress,
            viaDetailAddress = viaDetailAddress,
            viaZipCode = viaZipCode,
            viaSigungu = viaSigungu,
            viaSido = viaSido,
            viaLatitude = viaLatitude,
            viaLongitude = viaLongitude,
            distance = distance,
            originalPrice = originalPrice,
            fee = fee,
            driverPrice = driverPrice,
            loadingMethod = loadingMethod,
            itemName = itemName,
            itemCount = itemCount,
            palletCount = palletCount,
            franchisee = franchisee,
            precautions = precautions,
            memo = memo,
            dispatchStart = dispatchStart,
            dispatchEnd = dispatchEnd,
            paymentType = paymentType,
            addressType = addressType,
            dispatchCode = dispatchCode,
            shipPrice = shipPrice,
            status = status,
            user = user!!,
            finalApprovalUser = finalApprovalUser,
            parentCarType = parentCarType!!,
            driver = driver,
            dispatchDateTime = dispatchDateTime,
            adminGroup = adminGroup!!
        )
    }
}
