package com.ilogistic.delivery_admin_backend.dispatch.controller

import com.ilogistic.delivery_admin_backend.aop.PaymentCheck
import com.ilogistic.delivery_admin_backend.aop.UserRights
import com.ilogistic.delivery_admin_backend.aop.SuspendCheck
import com.ilogistic.delivery_admin_backend.dispatch.domain.dto.*
import com.ilogistic.delivery_admin_backend.dispatch.domain.entity.Dispatch
import com.ilogistic.delivery_admin_backend.dispatch.enums.DispatchStatus
import com.ilogistic.delivery_admin_backend.dispatch.service.DispatchService
import com.ilogistic.delivery_admin_backend.dto.common.PaginateDto
import com.ilogistic.delivery_admin_backend.user.enums.UserRole
import io.swagger.v3.oas.annotations.Operation
import org.springframework.data.domain.Page
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.security.Principal

@RequestMapping("/api/v1/dispatch")
@RestController
class DispatchController(
    private val dispatchService: DispatchService
) {

    @UserRights([UserRole.ADMIN, UserRole.CALL_WORKER])
//    @SuspendCheck
    @Operation(summary = "배차 등록", description = "배차를 등록하는 API")
    @PostMapping()
    fun create(@RequestBody dispatchRequestDto: DispatchRequestDto, principal: Principal) : ResponseEntity<Void>{
        dispatchService.create(dispatchRequestDto, principal.name.toLong())
        return ResponseEntity(HttpStatus.OK)
    }

    @UserRights([UserRole.FRANCHISEE])
//    @SuspendCheck
    @Operation(summary = "배차 등록", description = "배차를 등록하는 API")
    @PostMapping("/franchisee")
    fun franchiseeDispatchCreate(@RequestBody dispatchRequestDto: DispatchRequestDto, principal: Principal) : ResponseEntity<Void>{
        dispatchService.franchiseeDispatchCreate(dispatchRequestDto, principal.name.toLong())
        return ResponseEntity(HttpStatus.OK)
    }


    @UserRights([UserRole.ADMIN, UserRole.CALL_WORKER])
    @Operation(summary = "배차 요청 리스트", description = "화주가 요청한 배차리스트를 보여주는 API")
    @GetMapping("/request")
    fun getDispatchRequestList(dispatchRequestSearchDto: DispatchRequestSearchDto, paginateDto: PaginateDto, principal: Principal) : ResponseEntity<Page<DispatchRequestResponseDto>>{
        return ResponseEntity.ok(dispatchService.getDispatchRequestPageList(paginateDto, dispatchRequestSearchDto, principal.name.toLong()))
    }

    @UserRights([UserRole.ADMIN, UserRole.CALL_WORKER])
    @Operation(summary = "배차 요청 승인", description = "배차 요청을 승인해주는 API")
    @PostMapping("/request/approve")
    fun approveDispatchRequest(@RequestBody dispatchRequestApproveRequestDtoList: List<DispatchRequestApproveRequestDto>, principal: Principal) : ResponseEntity<Void>{
        dispatchService.dispatchApprove(dispatchRequestApproveRequestDtoList, principal.name.toLong())
        return ResponseEntity(HttpStatus.OK)
    }

    @UserRights
    @Operation(summary = "배차 리스트", description = "배차 리스트를 조회하는 API")
    @GetMapping()
    fun getDispatchList() : ResponseEntity<List<DispatchResponseDto>>{
        return ResponseEntity.ok(dispatchService.getDispatchList())
    }

    @UserRights([UserRole.ADMIN, UserRole.CALL_WORKER])
    @Operation(summary = "배차 리스트", description = "배차 리스트를 조회하는 API")
    @GetMapping("/company/waiting")
    fun getCompanyWaitDispatchList(principal: Principal) : ResponseEntity<List<DispatchResponseDto>>{
        return ResponseEntity.ok(dispatchService.getCompanyWaitDispatchList(principal.name.toLong()))
    }

    @UserRights([UserRole.DRIVER])
    @Operation(summary = "기사 대기 배차리스트", description = "기사 대기 배차리스트를 조회하는 API (기사의 차량 타입과 배차의 차량 타입이 일치하는 배차만 조회)")
    @GetMapping("/waiting/match-car-type")
    fun getDispatchWaitingListMatchingCarType(principal: Principal) : ResponseEntity<List<DispatchResponseDto>>{
        return ResponseEntity.ok(dispatchService.getDispatchWaitingListMatchingCarType(principal.name.toLong()))
    }

    @UserRights([UserRole.ADMIN, UserRole.FRANCHISEE, UserRole.DRIVER, UserRole.CALL_WORKER])
    @Operation(summary = "배차 디테일 조회", description = "배차 디테일을 조회하는 API")
    @GetMapping("/{id}")
    fun getDispatchDetail(@PathVariable id: Long) : ResponseEntity<DispatchResponseDto>{
        return ResponseEntity.ok(dispatchService.getDispatchDetail(id))
    }

    @UserRights([UserRole.ADMIN, UserRole.FRANCHISEE, UserRole.CALL_WORKER])
    @Operation(summary = "배차 수정시 필요한 정보", description = "배차 수정시 필요한 정보 API")
    @GetMapping("/{id}/modify")
    fun getDispatchModifyDetail(@PathVariable id: Long) : ResponseEntity<DispatchModifyResponseDto>{
        return ResponseEntity.ok(dispatchService.getDispatchModifyDetail(id))
    }

    @UserRights([UserRole.DRIVER])
    @Operation(summary = "배차 요청", description = "기사가 배차를 요청하는 API")
    @PaymentCheck
    @PostMapping("/{id}/request")
    fun dispatchRequest(@PathVariable id: Long, principal: Principal) : ResponseEntity<Void>{
        dispatchService.dispatchRequest(dispatchId = id, driverId = principal.name.toLong())
        return ResponseEntity(HttpStatus.OK)
    }

    @Operation(summary = "배차 상태로 조회", description = "기사의 배차 상태로 조회하는 API")
    @GetMapping("/status/{status}")
    fun getDispatchListByStatusAndDriver(@PathVariable status: String, principal: Principal) : ResponseEntity<List<DispatchResponseDto>>{
        return ResponseEntity.ok(dispatchService.getDispatchListByStatusAndDriver(DispatchStatus.findDispatchStatus(status), principal.name.toLong()))
    }

    @UserRights([UserRole.DRIVER])
    @Operation(summary = "배송 진행중인 배차리스트 조회", description = "배송 진행중인 배차리스트 조회하는 API")
    @GetMapping("/active")
    fun getActiveDispatchList(principal: Principal) : ResponseEntity<List<DispatchResponseDto>>{
        return ResponseEntity.ok(dispatchService.getActiveDispatchList(principal.name.toLong()))
    }


    @UserRights([UserRole.DRIVER])
    @Operation(summary = "배차 갯수", description = "기사의 배차 갯수를 조회하는 API")
    @GetMapping("/count")
    fun getDispatchCount(principal: Principal) : ResponseEntity<DispatchCountResponseDto>{
        return ResponseEntity.ok(dispatchService.getDispatchCount(principal.name.toLong()))
    }

    @UserRights([UserRole.DRIVER])
    @Operation(summary = "배차 취소", description = "기사가 배차를 취소했을 때 호출하는 API")
    @PostMapping("/{dispatchId}/cancel")
    fun dispatchCancel(@PathVariable dispatchId: Long, principal: Principal) : ResponseEntity<Void>{
        dispatchService.dispatchCancel(principal.name.toLong(), dispatchId)
        return ResponseEntity(HttpStatus.OK)
    }

    @UserRights([UserRole.DRIVER])
    @Operation(summary = "물류 상차 완료", description = "기사가 물류 상차를 완료했을 때 호출하는 API")
    @PostMapping("/{id}/loading")
    fun changeDispatchStatus(@PathVariable id: Long, principal: Principal) : ResponseEntity<Void>{
        dispatchService.dispatchLoading(id, principal.name.toLong())
        return ResponseEntity(HttpStatus.OK)
    }


    @UserRights([UserRole.DRIVER])
    @Operation(summary = "물류 운송 완료", description = "기사가 배차를 완료했을 때 호출하는 API")
    @PostMapping("/{id}/complete")
    fun dispatchComplete(@PathVariable id: Long, principal: Principal) : ResponseEntity<Void>{
        dispatchService.dispatchComplete(id, principal.name.toLong())
        return ResponseEntity(HttpStatus.OK)
    }

    @UserRights([UserRole.ADMIN, UserRole.CALL_WORKER])
    @Operation(summary = "직권 주기", description = "관리자가 기사에게 직권을 내주는 API")
    @PostMapping("/{driverId}/{dispatchId}/force")
    fun dispatchForceComplete(@PathVariable driverId: Long, @PathVariable dispatchId: Long) : ResponseEntity<Void>{
        dispatchService.forceDispatch(dispatchId, driverId)
        return ResponseEntity(HttpStatus.OK)
    }

    @UserRights([UserRole.ADMIN, UserRole.CALL_WORKER])
    @Operation(summary = "강제 취소", description = "관리자가 배차를 강제로 취소하는 API")
    @PostMapping("/{driverId}/{dispatchId}/cancel/force")
    fun dispatchForceCancel(@PathVariable driverId: Long, @PathVariable dispatchId: Long) : ResponseEntity<Void>{
        dispatchService.forceDispatchCancel(dispatchId, driverId)
        return ResponseEntity(HttpStatus.OK)
    }

    @UserRights([UserRole.ADMIN, UserRole.FRANCHISEE, UserRole.CALL_WORKER])
    @Operation(summary = "배차 리스트 조회 (페이징)", description = "관리자가 배차 리스트를 조회하는 API 화주가 검색할때는 화주의 배차건만, 어드민일때는 전체 배차건을 조회한다.")
    @GetMapping("/page")
    fun getDispatchPageList(paginateDto: PaginateDto, dispatchSearchDto: DispatchSearchDto, principal: Principal) : ResponseEntity<Page<DispatchPageListResponseDto>>{
        return ResponseEntity.ok(dispatchService.getDispatchPageList(paginateDto, dispatchSearchDto))
    }

    @UserRights([UserRole.FRANCHISEE])
    @Operation(summary = "배차 리스트 통계")
    @GetMapping("/page/statistics")
    fun getDispatchPageListStatistics( dispatchSearchDto: DispatchSearchDto, principal: Principal) : ResponseEntity<DispatchStatisticsResponseDto>{
        return ResponseEntity.ok(dispatchService.getDispatchPageListStatistics(dispatchSearchDto))
    }


    @UserRights([UserRole.ADMIN, UserRole.FRANCHISEE, UserRole.CALL_WORKER])
    @Operation(summary = "배차 수정", description = "관리자가 배차를 수정하는 API")
    @PutMapping("/{id}")
    fun modifyDispatch(@PathVariable id: Long, @RequestBody dispatchRequestDto: DispatchRequestDto) : ResponseEntity<Void>{
        dispatchService.modify(id, dispatchRequestDto)
        return ResponseEntity.ok().build()
    }

    @UserRights([UserRole.ADMIN, UserRole.CALL_WORKER])
    @Operation(summary = "배차 삭제", description = "관리자가 배차를 삭제하는 API")
    @DeleteMapping("/{ids}")
    fun deleteDispatch(@PathVariable ids: List<Long>) {
        dispatchService.delete(ids)
    }

    @UserRights([UserRole.FRANCHISEE])
    @Operation(summary = "화주 배차 5건 조회", description = "화주의 배차 5건을 조회하는 API")
    @GetMapping("/franchisee/main-screen")
    fun getFranchiseeMainScreenDispatchList(principal: Principal) : ResponseEntity<List<FranchiseeDispatchMainScreenResponseDto>>{
        return ResponseEntity.ok(dispatchService.getFranchiseeMainScreenDispatchList(principal.name.toLong()))
    }

    @GetMapping("/test")
    fun test() : ApprovalUserTestDto{
        return dispatchService.test()
    }

}
