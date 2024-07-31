package com.ilogistic.delivery_admin_backend.dispatch.domain.repository

import com.ilogistic.delivery_admin_backend.dispatch.domain.dto.*
import com.ilogistic.delivery_admin_backend.dispatch.enums.DispatchStatus
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable

interface DispatchRepositoryCustom {

    fun getDispatchList() : List<DispatchResponseDto>?

    fun getDispatchModifyDetail(id : Long) : DispatchModifyResponseDto?
    fun getDispatchWaitingListMatchingCarType(driverId: Long) : List<DispatchResponseDto>?

    fun getFranchiseeDispatchDriverIdList(franchiseeId: Long) : List<Long>?

    fun getDispatchDetail(id: Long) : DispatchResponseDto?

    fun getDispatchListByStatusAndDriver(status: DispatchStatus, driverId: Long) : List<DispatchResponseDto>?

    fun getActiveDispatchList(driverId: Long) : List<DispatchResponseDto>?

    fun getDispatchCount(driverId: Long?, status: DispatchStatus) : Long?

    fun getCheckDispatch(id: Long, driverId: Long) : DispatchCheckDto?

    fun getDispatchPageList(pageable: Pageable, dispatchSearchDto: DispatchSearchDto)  : Page<DispatchPageListResponseDto>

    fun getDispatchModifyInfo(dispatchId: Long): DispatchModifyInfoDto?

    fun getDispatchStatus(dispatchId: Long): DispatchStatus?

    fun getDispatchRequestPageList(pageable: Pageable, dispatchRequestSearchDto: DispatchRequestSearchDto, userId : Long) : Page<DispatchRequestResponseDto>

    //화주의 배차목록중에 드라이버가 운행중인 배차가 있는지 확인
    fun isDriverRunningBatch(driverId: Long, franchiseeId: Long) : Boolean

    fun getDispatchPageListStatistics(dispatchSearchDto: DispatchSearchDto) : DispatchStatisticsResponseDto?

    fun getDispatchDetailV2(id: Long) : DispatchResponseDtoV2?

    fun test(id: Long) : ApprovalUserTestDto?

    fun getFranchiseeId(id: Long) : Long

    fun getDispatchAdminGroupCode(id: Long) : String?

    fun getCompanyWaitDispatchList(id: Long) : List<DispatchResponseDto>?

    fun getFranchiseeMainScreenDispatchList(franchiseeId: Long) : List<FranchiseeDispatchMainScreenResponseDto>?

    fun getDispatchSocketSendInfoDto(id: Long) : DispatchSocketSendInfoDto
}
