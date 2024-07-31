package com.ilogistic.delivery_admin_backend.user.controller

import com.ilogistic.delivery_admin_backend.aop.UserRights
import com.ilogistic.delivery_admin_backend.dto.common.PaginateDto
import com.ilogistic.delivery_admin_backend.user.domain.dto.*
import com.ilogistic.delivery_admin_backend.user.enums.UserRole
import com.ilogistic.delivery_admin_backend.user.service.UserService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.data.domain.Page
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.security.Principal
import javax.validation.Valid

@Tag(name = "회원관련 API")
@RestController
class UserController(
        private val userService: UserService
) {

    @Operation(summary = "로그인 API")
    @PostMapping("/api/v1/login")
    fun login(@Valid @RequestBody loginRequestDto: LoginRequestDto): ResponseEntity<TokenResponseDto> {
        return ResponseEntity.ok(userService.login(loginRequestDto))
    }

//    @SuperAdminRights
    @Operation(summary = "슈퍼관리자 회원가입 API")
    @PostMapping("/api/v1/signup")
    fun signup(@Valid @RequestBody signupDto: SignupDto): ResponseEntity<UserResponseDto> {
        return ResponseEntity.ok(userService.signup(signupDto))
    }

//    @com.ilogistic.delivery_admin_backend.aop.SuperAdminRights
    @Operation(summary = "아이디 중복검사 API (중복시 true 반환)")
    @GetMapping("/api/v1/user/{username}/exists")
    fun existsByUsername(
        @Parameter(name = "username", required = false, example = "admin11", description = "중복 검사 할 아이디")
        @PathVariable username : String) : ResponseEntity<Boolean>{
        return ResponseEntity.ok(userService.existsByUsername(username))
    }

    @Operation(summary = "토큰 재발급 API")
    @PostMapping("/api/v1/reissue")
    fun reissue(@RequestBody reissueDto: ReissueDto): ResponseEntity<TokenResponseDto> {
        return ResponseEntity.ok(userService.reissue(reissueDto))
    }

//    @com.ilogistic.delivery_admin_backend.aop.AdminRights
    @Operation(summary = "유저 정보 API")
    @GetMapping("/api/v1/user-info")
    fun getUserInfo(principal: Principal): ResponseEntity<Any> {
    return ResponseEntity.ok(userService.getUserInfo(principal.name.toLong()))
    }

    @UserRights([UserRole.SUPER_ADMIN])
    @Operation(summary = "유저 정보 API (SUPER ADMIN 전용)")
    @GetMapping("/api/v1/user-info/{id}")
    fun getUserInfo(@PathVariable id : Long): ResponseEntity<Any> {
        return ResponseEntity.ok(userService.getUserInfo(id))
    }

    @UserRights([UserRole.SUPER_ADMIN])
    @Operation(summary = "비밀번호 변경 API")
    @PutMapping("/api/v1/password")
    fun passwordChange(
        @RequestBody passwordChangeDto: PasswordChangeDto, principal: Principal) : ResponseEntity<String>{
        userService.passwordChange(principal.name.toLong(), passwordChangeDto, false)
        return ResponseEntity.ok("비밀번호 변경이 완료되었습니다.")
    }

    @UserRights([UserRole.SUPER_ADMIN])
    @Operation(summary = "비밀번호 변경 API (SUPER ADMIN 전용)")
    @PutMapping("/api/v1/password/{id}")
    fun passwordChange(
        @Parameter(name = "id", required = false, example = "1",description = "비밀번호 변경 할 회원의 시퀀스 ID")
        @PathVariable id : Long,
        @RequestBody passwordChangeDtoV2: PasswordChangeDtoV2
    ) : ResponseEntity<String>{
        val passwordChangeDto = PasswordChangeDto(newPassword = passwordChangeDtoV2.password)
        userService.passwordChange(id, passwordChangeDto, true)
        return ResponseEntity.ok("비밀번호 변경이 완료되었습니다.")
    }

    @UserRights([UserRole.SUPER_ADMIN])
    @Operation(summary = "관리자 삭제 API")
    @DeleteMapping("/api/v1/user/{id}")
    fun delete(
        @Parameter(name = "id", required = false, example = "1",description = "삭제 할 회원의 시퀀스 ID")
        @PathVariable id : Long
    ) : ResponseEntity<String>{
        userService.deleteUser(id)
        return ResponseEntity.ok("삭제가 완료되었습니다.")
    }

    @UserRights([UserRole.SUPER_ADMIN])
    @Operation(summary = "관리자 생성 API")
    @PostMapping("/api/v1/user/admin")
    fun signupAdmin(@Valid @RequestBody adminSignupDto: AdminSignupDto): ResponseEntity<AdminResponseDto> {
        return ResponseEntity.ok(userService.signupAdmin(adminSignupDto))
    }

    @UserRights([UserRole.SUPER_ADMIN, UserRole.ADMIN, UserRole.CALL_WORKER])
    @Operation(summary = "화주 등록 API")
    @PostMapping("/api/v1/user/franchisee")
    fun signupFranchisee(@Valid @RequestBody franchiseeSignupDto: FranchiseeSignupDto, principal: Principal): ResponseEntity<Long> {
        return ResponseEntity.ok(userService.signupFranchisee(franchiseeSignupDto, principal.name.toLong()))
    }

    @UserRights([UserRole.SUPER_ADMIN, UserRole.ADMIN])
    @Operation(summary = "콜센터 직원 등록 API")
    @PostMapping("/api/v1/user/call-worker")
    fun signupCallWorker(@Valid @RequestBody callWorkerSignupDto: CallWorkerSignupDto, principal: Principal): ResponseEntity<Long> {
        return ResponseEntity.ok(userService.signupCallWorker(callWorkerSignupDto, principal.name.toLong()))
    }

    @UserRights([UserRole.SUPER_ADMIN, UserRole.ADMIN, UserRole.CALL_WORKER])
    @Operation(summary = "기사 등록 API")
    @PostMapping("/api/v1/user/driver")
    fun signupDriver(@Valid @RequestBody driverSignupDto: DriverSignupDto, principal: Principal): ResponseEntity<Long> {
        return ResponseEntity.ok(userService.signupDriver(driverSignupDto, principal.name.toLong()))
    }

    @UserRights([UserRole.ADMIN, UserRole.CALL_WORKER])
    @Operation(summary = "소속기사 목록 API")
    @GetMapping("/api/v1/user/company-driver")
    fun getDriverList(driverSearchDto: DriverSearchDto, paginateDto: PaginateDto, principal: Principal): ResponseEntity<Page<CompanyDriverResponseDto>> {
        return ResponseEntity.ok(userService.getCompanyDriverList(driverSearchDto, paginateDto, principal.name.toLong()))
    }

    @UserRights([UserRole.SUPER_ADMIN])
    @Operation(summary = "기사 목록 API")
    @GetMapping("/api/v1/user/driver")
    fun getDriverList(driverSearchDto: DriverSearchDto, paginateDto: PaginateDto): ResponseEntity<Page<DriverAllListResponseDto>> {
        return ResponseEntity.ok(userService.getDriverList(driverSearchDto, paginateDto))
    }

    @UserRights([UserRole.SUPER_ADMIN])
    @Operation(summary = "전체 유저 목록 API")
    @GetMapping("/api/v1/user")
    fun getUserList(userSearchDto: UserSearchDto, paginateDto: PaginateDto): ResponseEntity<Page<UserListResponseDto>> {
        return ResponseEntity.ok(userService.getUserList(userSearchDto, paginateDto))
    }

    @GetMapping("/api/v1/user/driver/{id}")
    fun getDriverDetail(@PathVariable id: Long): ResponseEntity<CompanyDriverResponseDto> {
        return ResponseEntity.ok(userService.getDriverDetail(id))
    }

    @PutMapping("/api/v1/user/driver/{id}")
    fun modifyDriver(@PathVariable id: Long, @RequestBody driverSignupDto: DriverSignupDto): ResponseEntity<String> {
        userService.modifyDriver(id, driverSignupDto)
        return ResponseEntity.ok("수정이 완료되었습니다.")
    }

    @GetMapping("/api/v1/user/franchisee")
    fun getFranchiseeList(franchiseeSearchDto: FranchiseeSearchDto, paginateDto: PaginateDto): ResponseEntity<Page<FranchiseSearchResponseDto>> {
        return ResponseEntity.ok(userService.getFranchiseeList(franchiseeSearchDto, paginateDto))
    }

    @GetMapping("/api/v1/user/driver-control/driver-info/{id}")
    fun getDriverControlDriverInfo(@PathVariable id: Long): ResponseEntity<DriverControlDriverInfoResponseDto> {
        return ResponseEntity.ok(userService.getDriverControlDriverInfo(id))
    }

}
