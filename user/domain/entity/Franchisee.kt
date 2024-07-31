package com.ilogistic.delivery_admin_backend.user.domain.entity

import com.fasterxml.jackson.annotation.JsonIgnore
import com.ilogistic.delivery_admin_backend.enums.Bank
import javax.persistence.*

@Entity
class Franchisee(
    @Id
    val id: Long? = null,
    val companyName: String,
    val companyNumber: String,
    val managerName: String,
//    val email: String,
    val managerPhone: String,
    val address: String,
    val detailAddress: String,
    val zipCode: String,
    val bankNumber: String,
//    val bankName: String,
    val bank: Bank,
    val bankOwner: String,
    val code: String,
//    val tel: String,
    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "create_user_id")
    var createUser: User? = null,
) {
}
