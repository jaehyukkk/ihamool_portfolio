package com.ilogistic.delivery_admin_backend.user.domain.dto

import com.ilogistic.delivery_admin_backend.enums.Bank

class FranchiseeResponseDto(
    val id: Long,
    val companyName: String,
    val companyNumber: String,
    val managerName: String,
    val managerPhone: String,
    val email: String,
    val address: String,
    val detailAddress: String,
    val zipCode: String,
    val bankNumber: String,
    val bank: Bank,
    val bankOwner: String,
    val tel: String
) {

    val bankName: String = bank.value
    val bankCode: Int = bank.code
}
