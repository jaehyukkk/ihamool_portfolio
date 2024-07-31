package com.ilogistic.delivery_admin_backend.dispatchcomplete.controller

import com.ilogistic.delivery_admin_backend.aop.UserRights
import com.ilogistic.delivery_admin_backend.dispatchcomplete.domain.dto.*
import com.ilogistic.delivery_admin_backend.dispatchcomplete.service.DispatchCompleteService
import com.ilogistic.delivery_admin_backend.dto.common.PaginateDto
import com.ilogistic.delivery_admin_backend.user.enums.UserRole
import io.swagger.v3.oas.annotations.Operation
import org.springframework.data.domain.Page
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.security.Principal
import javax.servlet.http.HttpServletResponse

@RequestMapping("/api/v1/dispatch-complete")
@RestController
class DispatchCompleteController(
    private val dispatchCompleteService: DispatchCompleteService
) {

    @GetMapping("/statistics")
    fun getStatistics(principal: Principal): ResponseEntity<DispatchCompleteStatisticsResponseDto> {
        val driverId = principal.name.toLong()
        return ResponseEntity.ok(dispatchCompleteService.getStatistics(driverId))
    }

    @UserRights([UserRole.DRIVER])
    @Operation(summary = "기사 배송 완료 리스트", description = "기사 배송 완료 리스트를 보여주는 API")
    @GetMapping("/screen/list")
    fun getDriverCompleteScreenList(principal: Principal): ResponseEntity<List<DriverCompleteScreenListResponseDto>> {
        val driverId = principal.name.toLong()
        return ResponseEntity.ok(dispatchCompleteService.getDriverCompleteScreenList(driverId))
    }

    @UserRights([UserRole.ADMIN, UserRole.CALL_WORKER])
    @Operation(summary = "협력사 총 통계", description = "협력사 총 통계를 보여주는 API (총건수, 총매출, 회사순익)")
    @GetMapping("/statistics/company/total")
    fun getCompanyTotalStatistics(statisticsSearchDto: StatisticsSearchDto, principal: Principal): ResponseEntity<CompanyTotalStatisticsResponseDto> {
        return ResponseEntity.ok(dispatchCompleteService.getCompanyTotalStatistics(statisticsSearchDto, principal.name.toLong()))
    }

    @UserRights([UserRole.ADMIN, UserRole.CALL_WORKER])
    @Operation(summary = "협력사 일자별 통계리스트", description = "협력사 일자별 통계리스트를 페이징처리해서 보여주는 API")
    @GetMapping("/statistics/company")
    fun getCompanyStatistics(statisticsSearchDto: StatisticsSearchDto, paginateDto: PaginateDto, principal: Principal): ResponseEntity<Page<StatisticsResponseDto>> {
        return ResponseEntity.ok(dispatchCompleteService.getCompanyStatistics(statisticsSearchDto, paginateDto, principal.name.toLong()))
    }

    @UserRights([UserRole.ADMIN, UserRole.CALL_WORKER])
    @Operation(summary = "협력사 일자별 통계리스트 엑셀 다운로드", description = "협력사 일자별 통계리스트 엑셀 다운로드 API")
    @GetMapping("/statistics/company/excel")
    fun companyStatisticsExcelDownload(response : HttpServletResponse, statisticsSearchDto: StatisticsSearchDto, principal: Principal): ResponseEntity<Void> {
        dispatchCompleteService.companyStatisticsExcelDownload(statisticsSearchDto, principal.name.toLong(), response)
        return ResponseEntity.ok().build()
    }

    @UserRights([UserRole.ADMIN, UserRole.CALL_WORKER])
    @Operation(summary = "기사별 매출 내역", description = "협력사 소속기사 매출 내역 API")
    @GetMapping("/statistics/driver")
    fun getDriverStatistics(
        driverStatisticsSearchDto: DriverStatisticsSearchDto,
        paginateDto: PaginateDto,
        principal: Principal
    ): ResponseEntity<Page<DriverStatisticsResponseDto>> {
        return ResponseEntity.ok(dispatchCompleteService.getDriverStatistics(driverStatisticsSearchDto, paginateDto, principal.name.toLong()))
    }

    @UserRights([UserRole.ADMIN, UserRole.CALL_WORKER])
    @Operation(summary = "기사별 매출 내역 엑셀다운로드", description = "협력사 소속기사 매출 내역 엑셀다운로드API")
    @GetMapping("/statistics/driver/excel")
    fun driverStatisticExcelDownload(
        response : HttpServletResponse,
        driverStatisticsSearchDto: DriverStatisticsSearchDto,
        principal: Principal
    ): ResponseEntity<Void> {
        dispatchCompleteService.driverStatisticsExcelDownload(driverStatisticsSearchDto, principal.name.toLong(), response)
        return ResponseEntity.ok().build()
    }

    @GetMapping("/statistics/driver/total/self")
    fun getDriverTotalSelfStatistics(principal: Principal, statisticsSearchDto: StatisticsSearchDto): ResponseEntity<DriverTotalStatisticsSelfResponseDto> {
        val driverId = principal.name.toLong()
        return ResponseEntity.ok(dispatchCompleteService.getDriverTotalSelfStatistics(driverId, statisticsSearchDto))
    }

    @GetMapping("/statistics/driver/self")
    fun getDriverSelfStatistics(principal: Principal, statisticsSearchDto: StatisticsSearchDto): ResponseEntity<List<DriverSelfStatisticsListResponseDto>> {
        val driverId = principal.name.toLong()
        return ResponseEntity.ok(dispatchCompleteService.getDriverSelfStatistics(driverId, statisticsSearchDto))
    }

    @GetMapping("/statistics/driver/daily")
    fun getDriverDailyStatisticsListResponseDto(principal: Principal, statisticsSearchDto: StatisticsSearchDto): ResponseEntity<List<DriverDailyStatisticsListResponseDto>> {
        val driverId = principal.name.toLong()
        return ResponseEntity.ok(dispatchCompleteService.getDriverDailyStatisticsListResponseDto(driverId, statisticsSearchDto))
    }
}
