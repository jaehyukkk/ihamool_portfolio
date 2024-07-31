package com.ilogistic.delivery_admin_backend.user.domain.dto

import com.ilogistic.delivery_admin_backend.enums.Bank
import com.ilogistic.delivery_admin_backend.worklog.enums.CommuteType
import com.querydsl.core.annotations.QueryProjection

data class DriverInfoResponseDto @QueryProjection constructor(
    val id: Long,
    val userRole: String,
    val username: String,
    val name: String,
    val phone: String,
    val bankNumber: String,
    val bank: Bank,
    val bankOwner: String,
    val address: String,
    val detailAddress: String,
    val zipCode: String,
    val code: String,
    val carNumber: String,
    val adminGroupCode: String,
    var commuteType: CommuteType? = null,
) {

    init {
        if (commuteType == null) {
            commuteType = CommuteType.OFF
        }
    }
}
