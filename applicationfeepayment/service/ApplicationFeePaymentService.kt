package com.ilogistic.delivery_admin_backend.applicationfeepayment.service

import com.ilogistic.delivery_admin_backend.applicationfeepayment.domain.dto.*
import com.ilogistic.delivery_admin_backend.applicationfeepayment.domain.entity.ApplicationFeePayment
import com.ilogistic.delivery_admin_backend.applicationfeepayment.domain.repository.ApplicationFeePaymentRepository
import com.ilogistic.delivery_admin_backend.applicationfeepayment.enums.PaymentStatus
import com.ilogistic.delivery_admin_backend.dto.common.DateSearchDto
import com.ilogistic.delivery_admin_backend.dto.common.PaginateDto
import com.ilogistic.delivery_admin_backend.exception.BaseException
import com.ilogistic.delivery_admin_backend.exception.ErrorCode
import com.ilogistic.delivery_admin_backend.user.service.UserService
import com.ilogistic.delivery_admin_backend.userpoint.domain.dto.UserPointRequestDto
import com.ilogistic.delivery_admin_backend.userpoint.domain.entity.UserPoint
import com.ilogistic.delivery_admin_backend.userpoint.enums.PointReason
import com.ilogistic.delivery_admin_backend.userpoint.service.UserPointService
import com.ilogistic.delivery_admin_backend.usersuspendstatus.service.UserSuspendStatusService
import org.springframework.data.domain.Page
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class ApplicationFeePaymentService(
    private val applicationFeePaymentRepository: ApplicationFeePaymentRepository,
    private val userPointService: UserPointService,
    private val userService: UserService,
//    private val userSuspendStatusService: UserSuspendStatusService

) {
    @Transactional
    fun payment(userId : Long) {
        val currentUserPoint = userPointService.getUserPoint(userId)
        if (currentUserPoint < 23000) {
            throw BaseException(ErrorCode.POINT_NOT_ENOUGH)
        }

        if(getPaymentCheck(userId)) {
            return
        }

        val userPoint = userPointService.deductPoint(
            UserPointRequestDto(
                userId = userId,
                point = 23000,
                reason = PointReason.APPLICATION_USE_FEE
            )
        )

        val applicationFeePayment = ApplicationFeePayment(
            userPoint = userPoint,
            user = userService.getUser(userId),
            status = PaymentStatus.COMPLETE
        )

        applicationFeePaymentRepository.save(applicationFeePayment)
    }

    fun paymentV2(userId: Long, userPoint : UserPoint) {

        val applicationFeePayment = ApplicationFeePayment(
            userPoint = userPoint,
            user = userService.getUser(userId),
            status = PaymentStatus.COMPLETE
        )

        applicationFeePaymentRepository.save(applicationFeePayment)
    }

    fun getPaymentCheck(userId: Long) : Boolean {
        return applicationFeePaymentRepository.getPaymentCheck(userId)
    }

    fun paymentProcedure() {
        applicationFeePaymentRepository.paymentProcedure()
    }

    fun getPaymentList(paginateDto: PaginateDto, applicationFeePaymentSearchDto: ApplicationFeePaymentSearchDto) : Page<ApplicationFeePaymentResponseDto>{
        return applicationFeePaymentRepository.getPaymentList(paginateDto.pageable(), applicationFeePaymentSearchDto)
    }

    fun getPaymentTotalStatistics(applicationFeePaymentSearchDto: ApplicationFeePaymentSearchDto) : ApplicationFeePaymentStatisticsResponseDto {
        return applicationFeePaymentRepository.getPaymentTotalStatistics(applicationFeePaymentSearchDto) ?: ApplicationFeePaymentStatisticsResponseDto(
            totalAmount = 0,
            totalCount = 0
        )
    }

    fun getPaymentListSelf(paginateDto: PaginateDto, userId: Long) : Page<ApplicationPaymentSelfResponseDto> {
        return applicationFeePaymentRepository.getPaymentListSelf(paginateDto.pageable(),userId)
    }

    fun getPaymentTodayStatistics(paginateDto: PaginateDto, searchDto: ApplicationFeePaymentStatisticsDaySearchDto) : Page<ApplicationFeePaymentStatisticsDayResponseDto> {
        return applicationFeePaymentRepository.getPaymentTodayStatistics(paginateDto.pageable(), searchDto)
    }
}
