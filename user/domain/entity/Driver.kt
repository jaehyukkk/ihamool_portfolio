package com.ilogistic.delivery_admin_backend.user.domain.entity

import com.fasterxml.jackson.annotation.JsonIgnore
import com.ilogistic.delivery_admin_backend.cartype.domain.entity.CarType
import com.ilogistic.delivery_admin_backend.enums.Bank
import javax.persistence.*

@Entity
class Driver(
    @Id
    val id: Long? = null,
    val name: String,
    val phone: String,
    val bankNumber: String,
//    val bankName: String,
    val bank: Bank,
    val bankOwner: String,
    val address: String,
    val detailAddress: String,
    val zipCode: String,
    val code: String,
    val carNumber: String,
    val companyName: String,
    val companyNumber: String,
    val companyAddress: String,
    val companyDetailAddress: String,
    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "create_user_id")
    val createUser: User? = null,

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "car_type_id")
    val carType: CarType? = null

) {
}
