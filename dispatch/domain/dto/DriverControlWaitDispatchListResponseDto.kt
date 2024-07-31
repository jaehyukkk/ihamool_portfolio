package com.ilogistic.delivery_admin_backend.dispatch.domain.dto

class DriverControlWaitDispatchListResponseDto(
    val id : Long,
    val startAddress : String,
    val endAddress : String,
    val weight : Int,
    val itemName : String,
    val distance : Double,
) {
}
