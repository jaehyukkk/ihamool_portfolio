package com.ilogistic.delivery_admin_backend.dispatch.controller

import com.ilogistic.delivery_admin_backend.aop.UserRights
import com.ilogistic.delivery_admin_backend.dispatch.domain.dto.DispatchResponseDtoV2
import com.ilogistic.delivery_admin_backend.dispatch.service.DispatchService
import com.ilogistic.delivery_admin_backend.user.enums.UserRole
import io.swagger.v3.oas.annotations.Operation
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v2/dispatch")
class DispatchControllerV2(
    private val dispatchService: DispatchService
) {

    @UserRights([UserRole.ADMIN, UserRole.FRANCHISEE])
    @Operation(summary = "배차 디테일 조회", description = "배차 디테일을 조회하는 API (기사 정보 포함)")
    @GetMapping("/{id}")
    fun getDispatchDetail(@PathVariable id: Long) : ResponseEntity<DispatchResponseDtoV2> {
        return ResponseEntity.ok(dispatchService.getDispatchDetailV2(id))
    }
}
