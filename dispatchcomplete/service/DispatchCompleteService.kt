package com.ilogistic.delivery_admin_backend.dispatchcomplete.service

import com.ilogistic.delivery_admin_backend.api.domain.dto.TaxInvoiceRequestDto
import com.ilogistic.delivery_admin_backend.dispatch.domain.entity.Dispatch
import com.ilogistic.delivery_admin_backend.dispatchcomplete.domain.dto.*
import com.ilogistic.delivery_admin_backend.dispatchcomplete.domain.entity.DispatchComplete
import com.ilogistic.delivery_admin_backend.dispatchcomplete.domain.repository.DispatchCompleteRepository
import com.ilogistic.delivery_admin_backend.dto.common.PaginateDto
import com.ilogistic.delivery_admin_backend.exception.BaseException
import com.ilogistic.delivery_admin_backend.exception.ErrorCode
import com.ilogistic.delivery_admin_backend.user.domain.entity.Driver
import com.ilogistic.delivery_admin_backend.utils.ExcelUtils
import org.springframework.data.domain.Page
import org.springframework.stereotype.Service
import javax.servlet.http.HttpServletResponse

@Service
class DispatchCompleteService(
    private val dispatchCompleteRepository: DispatchCompleteRepository,
    private val excelUtils: ExcelUtils
) {

    fun save(dispatch: Dispatch, driver: Driver) {
        dispatchCompleteRepository.save(DispatchComplete(dispatch = dispatch, driver = driver))
    }

    fun getStatistics(driverId: Long): DispatchCompleteStatisticsResponseDto {
        return dispatchCompleteRepository.dispatchCompleteStatistics(driverId) ?: DispatchCompleteStatisticsResponseDto(
            0,
            0,
            0,
            0.0
        )
    }

    //대기, 진행, 완료 중 완료 화면에 보여 줄 데이터를 가져온다.
    fun getDriverCompleteScreenList(dispatchId: Long): List<DriverCompleteScreenListResponseDto> {
        return dispatchCompleteRepository.getDriverCompleteScreenList(dispatchId)
    }

    fun getCompanyTotalStatistics(statisticsSearchDto: StatisticsSearchDto, adminId: Long) : CompanyTotalStatisticsResponseDto{
        return dispatchCompleteRepository.getCompanyTotalStatistics(statisticsSearchDto, adminId) ?: CompanyTotalStatisticsResponseDto(
            0,
            0,
            0
        )
    }

    fun getCompanyStatistics(statisticsSearchDto: StatisticsSearchDto, paginateDto: PaginateDto, adminId: Long) : Page<StatisticsResponseDto>{
        return dispatchCompleteRepository.getCompanyStatistics(statisticsSearchDto, paginateDto.pageable(), adminId)
    }

    fun companyStatisticsExcelDownload(statisticsSearchDto: StatisticsSearchDto, userId: Long, response: HttpServletResponse){
        val data = dispatchCompleteRepository.getCompanyStatisticsList(statisticsSearchDto, userId)
        excelUtils.downloadExcel(response, data, StatisticsResponseDto::class.java, "company_statistics")
    }

    fun getDriverStatistics(driverStatisticsSearchDto: DriverStatisticsSearchDto, paginateDto: PaginateDto, adminId: Long) : Page<DriverStatisticsResponseDto>{
        return dispatchCompleteRepository.getDriverStatistics(driverStatisticsSearchDto, paginateDto.pageable(), adminId)
    }

    fun driverStatisticsExcelDownload(driverStatisticsSearchDto: DriverStatisticsSearchDto, userId: Long, response: HttpServletResponse){
        val data = dispatchCompleteRepository.getDriverStatisticsList(driverStatisticsSearchDto, userId)
        excelUtils.downloadExcel(response, data, DriverStatisticsResponseDto::class.java, "driver_statistics")
    }

    fun getDriverTotalSelfStatistics(driverId: Long, statisticsSearchDto: StatisticsSearchDto): DriverTotalStatisticsSelfResponseDto {
        return dispatchCompleteRepository.getDriverTotalSelfStatistics(driverId, statisticsSearchDto) ?: DriverTotalStatisticsSelfResponseDto(
            0,
            0,
            0,
            0,
            0
        )
    }

    fun getDriverSelfStatistics(driverId: Long, statisticsSearchDto: StatisticsSearchDto): List<DriverSelfStatisticsListResponseDto> {
        return dispatchCompleteRepository.getDriverSelfStatistics(driverId, statisticsSearchDto) ?: emptyList()
    }

    fun getDriverDailyStatisticsListResponseDto(driverId: Long, statisticsSearchDto: StatisticsSearchDto): List<DriverDailyStatisticsListResponseDto> {
        return dispatchCompleteRepository.getDriverDailyStatisticsListResponseDto(driverId, statisticsSearchDto) ?: emptyList()
    }

    fun getDispatchCompleteAmount(dispatchId: Long, userId: Long) : DispatchCompleteAmountResponseDto{
        return dispatchCompleteRepository.getDispatchCompleteAmount(dispatchId, userId) ?: throw BaseException(ErrorCode.ENTITY_NOT_FOUND)
    }

    fun getTaxInvoiceInfo(dispatchId: Long, userId: Long): TaxInvoiceRequestDto {
        return dispatchCompleteRepository.getTaxInvoiceInfo(dispatchId, userId) ?: throw BaseException(ErrorCode.ENTITY_NOT_FOUND)
    }

    fun editTaxInvoiceInfo(id: Long) {
        dispatchCompleteRepository.editTaxInvoiceInfo(id)
    }
}
