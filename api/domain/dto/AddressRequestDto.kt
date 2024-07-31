package com.ilogistic.delivery_admin_backend.api.domain.dto

class AddressRequestDto(
//    val address: String? = null,
    val startAddress: String,
    val endAddress: String,
    val viaAddress: String? = null,
) {
}
