package com.ilogistic.delivery_admin_backend.user.domain.dto

import com.fasterxml.jackson.annotation.JsonIgnore
import com.ilogistic.delivery_admin_backend.cartype.domain.entity.CarType
import com.ilogistic.delivery_admin_backend.enums.Bank
import com.ilogistic.delivery_admin_backend.user.domain.entity.Admin
import com.ilogistic.delivery_admin_backend.user.domain.entity.Driver
import com.ilogistic.delivery_admin_backend.user.domain.entity.User
import com.ilogistic.delivery_admin_backend.user.enums.UserRole
import com.ilogistic.delivery_admin_backend.utils.Utils
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContextHolder

class DriverSignupDto(
//    val username: String,
//    val password: String,
    val name: String,
    val phone: String,
    val bankNumber: String,

    val bankOwner: String,
    val address: String,
    val detailAddress: String,
    val zipCode: String,
    val code: String,
    val carNumber: String,
    val carTypeId: Long,
    val companyNumber: String,
    val companyName: String,
    val companyAddress: String,
    val companyDetailAddress: String,
    var bank: Bank,
    @JsonIgnore
    var carType: CarType? = null,

//    var adminId: Long? = null,
    val adminGroupId: Long? = null
) {


    fun toEntity(id: Long, createUser: User?, carType : CarType): Driver {
        return Driver(
            id = id,
            name = name,
            phone = phone,
            bankNumber = bankNumber,
            bank = bank,
            bankOwner = bankOwner,
            address = address,
            detailAddress = detailAddress,
            zipCode = zipCode,
            code = code,
            carNumber = carNumber,
            createUser = createUser,
            carType = carType,
            companyNumber = companyNumber,
            companyName = companyName,
            companyAddress = companyAddress,
            companyDetailAddress = companyDetailAddress
        )
    }
}
