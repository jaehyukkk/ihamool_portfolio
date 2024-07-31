package com.ilogistic.delivery_admin_backend.user.domain.dto

import com.querydsl.core.annotations.QueryProjection

data class FranchiseSearchResponseDto @QueryProjection constructor(
    val id : Long,
    val companyName: String,
    val managerPhone: String,
    val address: String,
    val detailAddress: String,
) {
}
