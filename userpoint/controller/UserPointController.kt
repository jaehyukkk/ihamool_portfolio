package com.ilogistic.delivery_admin_backend.userpoint.controller

import com.ilogistic.delivery_admin_backend.aop.UserRights
import com.ilogistic.delivery_admin_backend.applicationfeepayment.domain.entity.ApplicationFeePayment
import com.ilogistic.delivery_admin_backend.user.enums.UserRole
import com.ilogistic.delivery_admin_backend.userpoint.domain.dto.UserPointRequestDto
import com.ilogistic.delivery_admin_backend.userpoint.domain.dto.UserPointResponseDto
import com.ilogistic.delivery_admin_backend.userpoint.domain.entity.UserPoint
import com.ilogistic.delivery_admin_backend.userpoint.service.UserPointService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.security.Principal

@RequestMapping("/api/v1/user-point")
@RestController
class UserPointController(
    private val userPointService: UserPointService
) {

    @UserRights([UserRole.SUPER_ADMIN])
    @PatchMapping("/charge")
    fun chargePoint(@RequestBody userPointRequestDto: UserPointRequestDto): ResponseEntity<UserPoint> {
        return ResponseEntity.ok(userPointService.chargePoint(userPointRequestDto))
    }

    @UserRights([UserRole.SUPER_ADMIN])
    @PatchMapping("/deduct")
    fun deductPoint(@RequestBody userPointRequestDto: UserPointRequestDto): ResponseEntity<UserPoint> {
        return ResponseEntity.ok(userPointService.deductPoint(userPointRequestDto))
    }

    @UserRights
    @GetMapping()
    fun getUserPoint(principal: Principal): ResponseEntity<Int> {
        return ResponseEntity.ok(userPointService.getUserPoint(principal.name.toLong()))
    }

    @UserRights([UserRole.SUPER_ADMIN])
    @GetMapping("/{userId}/history")
    fun getUserPointHistory(@PathVariable userId: Long): ResponseEntity<List<UserPointResponseDto>> {
        return ResponseEntity.ok(userPointService.getUserPointHistory(userId))
    }

}
