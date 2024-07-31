package com.ilogistic.delivery_admin_backend.applicationfeepayment.controller

import com.ilogistic.delivery_admin_backend.applicationfeepayment.domain.dto.*
import com.ilogistic.delivery_admin_backend.applicationfeepayment.service.ApplicationFeePaymentService
import com.ilogistic.delivery_admin_backend.dto.common.DateSearchDto
import com.ilogistic.delivery_admin_backend.dto.common.PaginateDto
import org.springframework.data.domain.Page
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.security.Principal

@RestController
@RequestMapping("/api/v1/application-fee-payment")
class ApplicationFeePaymentController(
    private val applicationFeePaymentService: ApplicationFeePaymentService
) {

    @PostMapping()
    fun payment(userId: Long) {
        applicationFeePaymentService.payment(userId)
    }

    @GetMapping
    fun getPaymentList(paginateDto: PaginateDto, applicationFeePaymentSearchDto: ApplicationFeePaymentSearchDto) : ResponseEntity<Page<ApplicationFeePaymentResponseDto>>{
        return ResponseEntity.ok(applicationFeePaymentService.getPaymentList(paginateDto, applicationFeePaymentSearchDto))
    }

    @GetMapping("/self")
    fun getPaymentListByUser(paginateDto: PaginateDto, principal : Principal) : ResponseEntity<Page<ApplicationPaymentSelfResponseDto>>{
        return ResponseEntity.ok(applicationFeePaymentService.getPaymentListSelf(paginateDto, principal.name.toLong()))
    }

    @GetMapping("/statistics/total")
    fun getPaymentTotalStatistics(applicationFeePaymentSearchDto: ApplicationFeePaymentSearchDto) : ResponseEntity<ApplicationFeePaymentStatisticsResponseDto>{
        return ResponseEntity.ok(applicationFeePaymentService.getPaymentTotalStatistics(applicationFeePaymentSearchDto))
    }

    @GetMapping("/statistics/today")
    fun getPaymentTodayStatistics(paginateDto: PaginateDto, searchDto: ApplicationFeePaymentStatisticsDaySearchDto) : ResponseEntity<Page<ApplicationFeePaymentStatisticsDayResponseDto>>{
        return ResponseEntity.ok(applicationFeePaymentService.getPaymentTodayStatistics(paginateDto, searchDto))
    }
}
