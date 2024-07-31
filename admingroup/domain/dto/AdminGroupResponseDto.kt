package com.ilogistic.delivery_admin_backend.admingroup.domain.dto

import com.querydsl.core.annotations.QueryProjection

data class AdminGroupResponseDto @QueryProjection constructor(
    val id: Long,
    val companyName: String,
    val companyNumber: String,
    val ceoName: String,
    val ceoPhone: String,
    val address: String,
    val detailAddress: String,
    val zipCode: String,
    val barobillId: String,
) {
}
