package com.ilogistic.delivery_admin_backend.dispatch.domain.dto

class DispatchRequestApproveRequestDto(
    val id: Long,
    val originalPrice: Int,
    val fee: Int,
    val shipPrice: Int
) {
}
