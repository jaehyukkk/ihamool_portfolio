package com.ilogistic.delivery_admin_backend.user.domain.dto

import com.ilogistic.delivery_admin_backend.enums.Bank
import com.querydsl.core.annotations.QueryProjection

data class CompanyDriverResponseDto @QueryProjection constructor(
    val id: Long,
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
    val carTypeId: Long,
    val carTypeName: String,
    val parentCarTypeId: Long,
    val parentCarTypeName: String,
    val companyNumber: String,
    val companyName: String,
    val companyAddress: String,
    val companyDetailAddress: String,
) {
}
