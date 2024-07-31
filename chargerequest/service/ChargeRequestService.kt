package com.ilogistic.delivery_admin_backend.chargerequest.service

import com.ilogistic.delivery_admin_backend.chargerequest.domain.dto.ChargeRequestApproveRequestDto
import com.ilogistic.delivery_admin_backend.chargerequest.domain.dto.ChargeRequestRequestDto
import com.ilogistic.delivery_admin_backend.chargerequest.domain.dto.ChargeRequestSearchDto
import com.ilogistic.delivery_admin_backend.chargerequest.domain.entity.ChargeRequest
import com.ilogistic.delivery_admin_backend.chargerequest.domain.repository.ChargeRequestRepository
import com.ilogistic.delivery_admin_backend.chargerequest.enums.ChargeRequestStatus
import com.ilogistic.delivery_admin_backend.dto.common.PaginateDto
import com.ilogistic.delivery_admin_backend.exception.BaseException
import com.ilogistic.delivery_admin_backend.exception.ErrorCode
import com.ilogistic.delivery_admin_backend.user.service.UserService
import com.ilogistic.delivery_admin_backend.userpoint.domain.dto.UserPointRequestDto
import com.ilogistic.delivery_admin_backend.userpoint.enums.PointReason
import com.ilogistic.delivery_admin_backend.userpoint.service.UserPointService
import org.springframework.stereotype.Service
import javax.transaction.Transactional

@Service
class ChargeRequestService(
    private val chargeRequestRepository: ChargeRequestRepository,
    private val userService: UserService,
    private val userPointService: UserPointService
) {

    fun create(userId: Long, chargeRequestRequestDto: ChargeRequestRequestDto): ChargeRequest {
        val chargeRequest = ChargeRequest(
            point = chargeRequestRequestDto.point,
            user = userService.getUser(userId)
        )
        return chargeRequestRepository.save(chargeRequest)
    }

    @Transactional
    fun approve(chargeRequestApproveRequestDto: ChargeRequestApproveRequestDto) {
        val chargeRequest = chargeRequestRepository.getApproveTargetChargeRequest(chargeRequestApproveRequestDto)
            ?: throw BaseException(ErrorCode.NOT_FOUND_CHARGE_REQUEST)

        val userPoint = userPointService.chargePoint(
            UserPointRequestDto(
                userId = chargeRequest.user.id!!,
                point = chargeRequest.point,
                reason = PointReason.SELF_CHARGE
            )
        )

        val result = chargeRequestRepository.approve(
            chargeRequestId = chargeRequest.id!!
            , userPoint = userPoint
            , chargeRequestStatus = ChargeRequestStatus.ACCEPTED
        )

        if(result == 0) {
            throw BaseException(ErrorCode.NOT_FOUND_CHARGE_REQUEST)
        }
    }

    fun getChargeRequestList(userId: Long, paginateDto: PaginateDto) = chargeRequestRepository.getChargeRequestList(paginateDto.pageable(), userId)

    fun getAllChargeRequestList(paginateDto: PaginateDto, chargeRequestSearchDto: ChargeRequestSearchDto) = chargeRequestRepository.getAllChargeRequestList(paginateDto.pageable(), chargeRequestSearchDto)

    fun sum(chargeRequestSearchDto: ChargeRequestSearchDto) = chargeRequestRepository.sum(chargeRequestSearchDto)
}
