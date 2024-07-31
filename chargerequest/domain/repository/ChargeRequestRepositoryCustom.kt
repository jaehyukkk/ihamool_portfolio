package com.ilogistic.delivery_admin_backend.chargerequest.domain.repository

import com.ilogistic.delivery_admin_backend.chargerequest.domain.dto.ChargeRequestAllUserListResponseDto
import com.ilogistic.delivery_admin_backend.chargerequest.domain.dto.ChargeRequestApproveRequestDto
import com.ilogistic.delivery_admin_backend.chargerequest.domain.dto.ChargeRequestListResponseDto
import com.ilogistic.delivery_admin_backend.chargerequest.domain.dto.ChargeRequestSearchDto
import com.ilogistic.delivery_admin_backend.chargerequest.domain.entity.ChargeRequest
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable

interface ChargeRequestRepositoryCustom {

    fun getApproveTargetChargeRequest(chargeRequestApproveRequestDto: ChargeRequestApproveRequestDto): ChargeRequest?

    fun getChargeRequestList(pageable: Pageable, userId: Long): Page<ChargeRequestListResponseDto>

    fun getAllChargeRequestList(pageable: Pageable, chargeRequestSearchDto: ChargeRequestSearchDto): Page<ChargeRequestAllUserListResponseDto>

    fun sum(chargeRequestSearchDto: ChargeRequestSearchDto): Int

}
