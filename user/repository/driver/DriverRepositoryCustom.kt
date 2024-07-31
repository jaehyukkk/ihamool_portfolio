package com.ilogistic.delivery_admin_backend.user.repository.driver

import com.ilogistic.delivery_admin_backend.user.domain.dto.*
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable

interface DriverRepositoryCustom {

    fun getCompanyDriverList(driverSearchDto: DriverSearchDto, pageable: Pageable, searchUserId: Long): Page<CompanyDriverResponseDto>

    fun getDriverList(driverSearchDto: DriverSearchDto, pageable: Pageable): Page<DriverAllListResponseDto>

    fun getDriverDetail(driverId: Long): CompanyDriverResponseDto

    fun getDriverInfo(driverId: Long): DriverInfoResponseDto

    fun getDriverControlDriverInfo(driverId: Long): DriverControlDriverInfoResponseDto

    fun getDriverRedisSaveDto(driverId: Long): DriverRedisSaveDto

    fun getDriverCarTypeId(driverId: Long): Long
}
