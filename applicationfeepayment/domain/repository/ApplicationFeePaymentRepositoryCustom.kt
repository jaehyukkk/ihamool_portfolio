package com.ilogistic.delivery_admin_backend.applicationfeepayment.domain.repository

import com.ilogistic.delivery_admin_backend.applicationfeepayment.domain.dto.*
import com.ilogistic.delivery_admin_backend.dto.common.DateSearchDto
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import java.security.Principal

interface ApplicationFeePaymentRepositoryCustom {

    fun getPaymentCheck(userId: Long): Boolean

    fun getPaymentList(pageable: Pageable, applicationFeePaymentSearchDto: ApplicationFeePaymentSearchDto) : Page<ApplicationFeePaymentResponseDto>

    fun getPaymentTotalStatistics(applicationFeePaymentSearchDto: ApplicationFeePaymentSearchDto): ApplicationFeePaymentStatisticsResponseDto?

    fun getPaymentTodayStatistics(pageable: Pageable, searchDto: ApplicationFeePaymentStatisticsDaySearchDto): Page<ApplicationFeePaymentStatisticsDayResponseDto>

    fun getPaymentListSelf(pageable: Pageable, userId: Long): Page<ApplicationPaymentSelfResponseDto>
}
