package com.ilogistic.delivery_admin_backend.dispatchcomplete.domain.repository

import com.ilogistic.delivery_admin_backend.api.domain.dto.TaxInvoiceRequestDto
import com.ilogistic.delivery_admin_backend.dispatchcomplete.domain.dto.*
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable

interface DispatchCompleteRepositoryCustom {

    fun dispatchCompleteStatistics(driverId: Long): DispatchCompleteStatisticsResponseDto?

    fun getDriverCompleteScreenList(driverId: Long): List<DriverCompleteScreenListResponseDto>

    fun getCompanyTotalStatistics(statisticsSearchDto: StatisticsSearchDto, adminId: Long) : CompanyTotalStatisticsResponseDto?

    fun getCompanyStatistics(statisticsSearchDto: StatisticsSearchDto, pageable: Pageable, adminId: Long) : Page<StatisticsResponseDto>

    fun getCompanyStatisticsList(statisticsSearchDto: StatisticsSearchDto, userId: Long) : List<StatisticsResponseDto>

    fun getDriverStatistics(driverStatisticsSearchDto: DriverStatisticsSearchDto, pageable: Pageable, adminId:Long) : Page<DriverStatisticsResponseDto>

    fun getDriverStatisticsList(driverStatisticsSearchDto: DriverStatisticsSearchDto, userId:Long) : List<DriverStatisticsResponseDto>

    fun getDriverTotalSelfStatistics(driverId: Long, statisticsSearchDto: StatisticsSearchDto ): DriverTotalStatisticsSelfResponseDto?

    fun getDriverSelfStatistics(driverId: Long, statisticsSearchDto: StatisticsSearchDto) : List<DriverSelfStatisticsListResponseDto>?

    fun getDriverDailyStatisticsListResponseDto(driverId: Long, statisticsSearchDto: StatisticsSearchDto) : List<DriverDailyStatisticsListResponseDto>

    fun getDispatchCompleteAmount(dispatchId: Long, userId : Long): DispatchCompleteAmountResponseDto?

    fun getTaxInvoiceInfo(dispatchId: Long, userId: Long): TaxInvoiceRequestDto?
}
