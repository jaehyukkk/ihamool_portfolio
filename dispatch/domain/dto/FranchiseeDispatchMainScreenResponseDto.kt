package com.ilogistic.delivery_admin_backend.dispatch.domain.dto

import com.ilogistic.delivery_admin_backend.dispatch.enums.DispatchStatus

class FranchiseeDispatchMainScreenResponseDto(
    val id : Long,
    val status: DispatchStatus,
    val startSigungu: String,
    var startSido: String,
    val endSigungu: String,
    var endSido: String,
    val distance: Double,
    val itemName: String
) {
}
