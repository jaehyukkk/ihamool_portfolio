package com.ilogistic.delivery_admin_backend.chargerequest.controller

import com.ilogistic.delivery_admin_backend.aop.UserRights
import com.ilogistic.delivery_admin_backend.chargerequest.domain.dto.*
import com.ilogistic.delivery_admin_backend.chargerequest.service.ChargeRequestService
import com.ilogistic.delivery_admin_backend.dto.common.PaginateDto
import com.ilogistic.delivery_admin_backend.user.enums.UserRole
import org.springframework.data.domain.Page
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.security.Principal

@RequestMapping("/api/v1/charge-request")
@RestController
class ChargeRequestController(
    private val chargeRequestService: ChargeRequestService,
) {

    @UserRights([UserRole.SUPER_ADMIN])
    @PostMapping("/approve")
    fun approve(@RequestBody chargeRequestApproveRequestDto: ChargeRequestApproveRequestDto): ResponseEntity<Void> {
        chargeRequestService.approve(chargeRequestApproveRequestDto)
        return ResponseEntity.ok().build()
    }

    @UserRights
    @PostMapping()
    fun create(principal: Principal, @RequestBody chargeRequestRequestDto: ChargeRequestRequestDto): ResponseEntity<Void> {
        chargeRequestService.create(principal.name.toLong(), chargeRequestRequestDto)
        return ResponseEntity.ok().build()
    }

    @UserRights
    @GetMapping()
    fun getChargeRequestList(principal: Principal, paginateDto: PaginateDto): ResponseEntity<Page<ChargeRequestListResponseDto>> {
        return ResponseEntity.ok(chargeRequestService.getChargeRequestList(principal.name.toLong(), paginateDto))
    }

    @UserRights([UserRole.SUPER_ADMIN])
    @GetMapping("/all")
    fun getAllChargeRequestList(paginateDto: PaginateDto, chargeRequestSearchDto: ChargeRequestSearchDto): ResponseEntity<Page<ChargeRequestAllUserListResponseDto>> {
        return ResponseEntity.ok(chargeRequestService.getAllChargeRequestList(paginateDto, chargeRequestSearchDto))
    }

    @UserRights([UserRole.SUPER_ADMIN])
    @GetMapping("/sum")
    fun getStatistics(chargeRequestSearchDto: ChargeRequestSearchDto): ResponseEntity<Int> {
        return ResponseEntity.ok(chargeRequestService.sum(chargeRequestSearchDto))
    }

}
