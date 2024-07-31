package com.ilogistic.delivery_admin_backend.user.domain.dto

import com.fasterxml.jackson.annotation.JsonIgnore
import com.ilogistic.delivery_admin_backend.enums.Bank
import com.ilogistic.delivery_admin_backend.user.domain.entity.Admin
import com.ilogistic.delivery_admin_backend.user.domain.entity.Franchisee
import com.ilogistic.delivery_admin_backend.user.domain.entity.User

class FranchiseeSignupDto(
//    val username: String,
//    val password: String,
    val companyName: String,
    val companyNumber: String,
    val managerName: String,
    val managerPhone: String,
//    val email: String,
    val address: String,
    val detailAddress: String,
    val zipCode: String,
    val bankNumber: String,
    val bankCode: Int,
    val bankOwner: String,
//    val tel: String,
    val code: String,
    @JsonIgnore
    var bank: Bank? = null
) {

    init{
        bank = Bank.findByCode(bankCode)
    }

    fun toEntity(id: Long, createUser: User) : Franchisee {
        return Franchisee(
            id = id,
            companyName = companyName,
            companyNumber = companyNumber,
            managerName = managerName,
            managerPhone = managerPhone,
//            email = email,
            address = address,
            detailAddress = detailAddress,
            zipCode = zipCode,
            bankNumber = bankNumber,
            bank = bank!!,
            bankOwner = bankOwner,
            code = code,
//            tel = tel,
            createUser = createUser
        )
    }
}
