package com.ilogistic.delivery_admin_backend.location.service

import com.ilogistic.delivery_admin_backend.dispatch.service.DispatchService
import com.ilogistic.delivery_admin_backend.dto.common.LocationResponseDto
import com.ilogistic.delivery_admin_backend.exception.BaseException
import com.ilogistic.delivery_admin_backend.exception.ErrorCode
import com.ilogistic.delivery_admin_backend.redis.service.RedisService
import com.ilogistic.delivery_admin_backend.user.enums.UserRole
import com.ilogistic.delivery_admin_backend.user.service.UserService
import com.ilogistic.delivery_admin_backend.utils.UserRoleContext
import com.ilogistic.delivery_admin_backend.utils.Utils
import org.springframework.security.core.Authentication
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Service

@Service
class LocationService(
    private val redisService: RedisService,
    private val dispatchService: DispatchService,
    private val userService: UserService
) {

    fun getDriverLocationList() : List<LocationResponseDto>{
        val authentication: Authentication = SecurityContextHolder.getContext().authentication
        val isSuperAdmin = Utils.roleCheck(authentication, listOf(UserRole.SUPER_ADMIN))
        val locationList = redisService.getObjectListByPattern("LOCATION", LocationResponseDto::class.java)
        if(!isSuperAdmin){
            val groupCode = userService.getUserAdminGroupCode(authentication.name.toLong())
            return locationList.filter { it.adminGroupCode == groupCode }
        }
        return locationList
    }

    fun getDriverLocation(driverId: Long, id: Long) : LocationResponseDto {
        val authentication: Authentication = SecurityContextHolder.getContext().authentication
        val isFranchisee = Utils.roleCheck(authentication, listOf(UserRole.FRANCHISEE))
        if(isFranchisee){
            if(!dispatchService.isDriverRunningBatch(driverId, id)){
                throw BaseException(ErrorCode.FORBIDDEN)
            }
        }

        return redisService.getObjectValue("LOCATION-$driverId", LocationResponseDto::class.java)
            ?: throw BaseException(ErrorCode.ENTITY_NOT_FOUND)
    }
}
