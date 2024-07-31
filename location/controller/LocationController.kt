package com.ilogistic.delivery_admin_backend.location.controller

import com.ilogistic.delivery_admin_backend.aop.UserRights
import com.ilogistic.delivery_admin_backend.dto.common.LocationResponseDto
import com.ilogistic.delivery_admin_backend.location.service.LocationService
import com.ilogistic.delivery_admin_backend.user.enums.UserRole
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.security.Principal

@RequestMapping("/api/v1/location")
@RestController
class LocationController(
    private val locationService: LocationService
) {
    @UserRights([UserRole.SUPER_ADMIN, UserRole.ADMIN, UserRole.CALL_WORKER])
    @GetMapping()
    fun getDriverLocationList() : ResponseEntity<List<LocationResponseDto>> {
        println("getDriverLocationList")
        return ResponseEntity.ok(locationService.getDriverLocationList())
    }

    @UserRights([UserRole.ADMIN, UserRole.FRANCHISEE, UserRole.CALL_WORKER])
    @GetMapping("/{driverId}")
    fun getDriverLocation(@PathVariable driverId: Long, principal: Principal) : ResponseEntity<LocationResponseDto> {
        return ResponseEntity.ok(locationService.getDriverLocation(driverId, principal.name.toLong()))
    }
}
