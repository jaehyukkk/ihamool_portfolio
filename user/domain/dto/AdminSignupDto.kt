package com.ilogistic.delivery_admin_backend.user.domain.dto

import com.ilogistic.delivery_admin_backend.user.domain.entity.Admin
import io.swagger.v3.oas.annotations.media.Schema

class AdminSignupDto (
    @Schema(example = "admin11")
    val username: String,
    @Schema(example = "1234")
    val password: String,
    @Schema(example = "아이로지스틱")
    val companyName: String,
    @Schema(example = "4248100569")
    val companyNumber: String,
    @Schema(example = "김대표")
    val ceoName: String,
    @Schema(example = "01012345678")
    val ceoPhone: String,
    @Schema(example = "제주도 제주시 서사로 96")
    val address: String,
    @Schema(example = "서림아이빌 701호")
    val detailAddress: String,
    @Schema(example = "63001")
    val zipCode: String,
    @Schema(example = "")
    val barobillId: String? = null,
){

    fun toEntity(id: Long) : Admin {
        return Admin(
            id = id,
        )
    }
}
