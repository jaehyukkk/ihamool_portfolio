package com.ilogistic.delivery_admin_backend.dispatch.domain.dto

import com.ilogistic.delivery_admin_backend.dispatch.enums.DispatchStatus

class DispatchSocketSendInfoDto(
    val id: Long,
    val dispatchCode: String,
    val franchiseeId: Long,
    val adminGroupCode: String,
    val status : DispatchStatus,
    val driverName: String? = null,
    val carTypeList : Set<CarTypeIdResponse> ? = null,
    ) {

    companion object{
        class CarTypeIdResponse(
            val id: Long,
        )
    }
}
