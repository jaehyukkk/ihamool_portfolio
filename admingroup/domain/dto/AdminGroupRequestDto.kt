package com.ilogistic.delivery_admin_backend.admingroup.domain.dto

import com.fasterxml.jackson.annotation.JsonIgnore
import com.ilogistic.delivery_admin_backend.admingroup.domain.entity.AdminGroup
import com.ilogistic.delivery_admin_backend.utils.Utils
import io.swagger.v3.oas.annotations.media.Schema

class AdminGroupRequestDto(
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

) {
    @JsonIgnore
    val groupCode: String = Utils.generateRandomCode()
    fun toEntity(): AdminGroup {
        return AdminGroup(
            companyName = companyName,
            companyNumber = companyNumber,
            ceoName = ceoName,
            ceoPhone = ceoPhone,
            address = address,
            detailAddress = detailAddress,
            zipCode = zipCode,
            barobillId = barobillId,
            groupCode = groupCode
        )
    }
}
