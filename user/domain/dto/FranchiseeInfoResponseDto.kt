package com.ilogistic.delivery_admin_backend.user.domain.dto

import com.ilogistic.delivery_admin_backend.enums.Bank
import com.ilogistic.delivery_admin_backend.user.enums.UserRole
import com.querydsl.core.annotations.QueryProjection

data class FranchiseeInfoResponseDto @QueryProjection constructor(

    val id: Long,
    val companyName: String,
    val companyNumber: String,
    val managerName: String,
    val managerPhone: String,
    val address: String,
    val detailAddress: String,
    val zipCode: String,
    val bankNumber: String,
    val bank: Bank,
    val bankOwner: String,
){

    val userRole = UserRole.FRANCHISEE.value
    val bankName: String = bank.value
    val bankCode: Int = bank.code
}
