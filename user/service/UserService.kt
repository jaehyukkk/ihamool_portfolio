package com.ilogistic.delivery_admin_backend.user.service

import com.ilogistic.delivery_admin_backend.admingroup.domain.dto.AdminGroupRequestDto
import com.ilogistic.delivery_admin_backend.admingroup.domain.entity.AdminGroup
import com.ilogistic.delivery_admin_backend.admingroup.service.AdminGroupService
import com.ilogistic.delivery_admin_backend.cartype.service.CarTypeService
import com.ilogistic.delivery_admin_backend.dto.common.PaginateDto
import com.ilogistic.delivery_admin_backend.exception.BaseException
import com.ilogistic.delivery_admin_backend.exception.ErrorCode
import com.ilogistic.delivery_admin_backend.jwt.TokenProvider
import com.ilogistic.delivery_admin_backend.redis.service.RedisService
import com.ilogistic.delivery_admin_backend.user.domain.dto.*
import com.ilogistic.delivery_admin_backend.user.domain.entity.Admin
import com.ilogistic.delivery_admin_backend.user.domain.entity.Driver
import com.ilogistic.delivery_admin_backend.user.domain.entity.Franchisee
import com.ilogistic.delivery_admin_backend.user.domain.entity.User
import com.ilogistic.delivery_admin_backend.user.enums.UserRole
import com.ilogistic.delivery_admin_backend.user.repository.admin.AdminRepository
import com.ilogistic.delivery_admin_backend.user.repository.callworker.CallWorkerRepository
import com.ilogistic.delivery_admin_backend.user.repository.driver.DriverRepository
import com.ilogistic.delivery_admin_backend.user.repository.franchisee.FranchiseeRepository
import com.ilogistic.delivery_admin_backend.user.repository.user.UserRepository
import com.ilogistic.delivery_admin_backend.utils.Utils
import org.springframework.data.domain.Page
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.core.Authentication
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import java.util.concurrent.TimeUnit
import javax.transaction.Transactional

@Service
class UserService(
    private val userRepository: UserRepository,
    private val passwordEncoder: PasswordEncoder,
    private val authenticationManagerBuilder : AuthenticationManagerBuilder,
    private val tokenProvider: TokenProvider,
    private val adminRepository: AdminRepository,
    private val franchiseeRepository: FranchiseeRepository,
    private val driverRepository: DriverRepository,
    private val carTypeService: CarTypeService,
    private val redisService: RedisService,
    private val adminGroupService: AdminGroupService,
    private val callWorkerRepository: CallWorkerRepository,
) {

    @Transactional
    fun signup(signupDto: SignupDto): UserResponseDto {
        if (existsByUsername(signupDto.username)) {
            throw BaseException(ErrorCode.DUPLICATE_USER_ID)
        }
        signupDto.password = passwordEncoder.encode(signupDto.password)
        return UserResponseDto.of(userRepository.save(signupDto.toEntity()))
    }

    fun findByUsername(username: String) = userRepository.findByUsername(username)

    @Transactional
    fun signup(username: String, password:String, role: UserRole, adminGroup : AdminGroup?, depositName: String?): Long {
        if (existsByUsername(username)) {
            throw BaseException(ErrorCode.DUPLICATE_ID)
        }
        val encodePassword = passwordEncoder.encode(password)
        return UserResponseDto.of(userRepository.save(
            User(username = username
                , password = encodePassword
                , userRole = role.value
                , adminGroup = adminGroup
                , depositName = depositName
            )
        )

        ).id ?: throw BaseException(ErrorCode.INTERNAL_SERVER_ERROR)
    }

    @Transactional
    fun login(loginRequestDto: LoginRequestDto) : TokenResponseDto {
        val authenticationToken: UsernamePasswordAuthenticationToken = loginRequestDto.toAuthentication()
        val authentication: Authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken)
        val tokenResponseDto = tokenProvider.generateTokenDto(authentication)
        saveRefreshToken(authentication.name, tokenResponseDto)
        return tokenResponseDto
    }

    private fun saveRefreshToken(userId: String, tokenDto: TokenResponseDto) {
        redisService.setValues(
            "RTK:$userId",
            tokenDto.refreshToken!!,
            tokenDto.refreshTokenExpiresIn!!,
            TimeUnit.MILLISECONDS
        )
    }

    @Transactional
    fun reissue(reissueDto: ReissueDto) : TokenResponseDto {
        if (!tokenProvider.validateToken(reissueDto.refreshToken)) {
            throw BaseException(ErrorCode.INVALID_REFRESH_TOKEN)
        }

        val authentication = tokenProvider.getAuthentication(reissueDto.accessToken)
//        val refreshToken = refreshTokenRepository.findByKey(authentication.name.toLong()) ?: throw BaseException(ErrorCode.LOGOUT_USER)
        val refreshToken = redisService.getValues("RTK:" + authentication.name) as String? ?: throw BaseException(ErrorCode.LOGOUT_USER)

        if (reissueDto.refreshToken != refreshToken) {
            throw BaseException(ErrorCode.NOT_MATCH_USER)
        }

        return tokenProvider.generateOnlyAccessToken(authentication)
    }

    fun getUser(id: Long) : User {
        return userRepository.findById(id).orElseThrow()
    }

    fun getUserInfo(id: Long) : Any {
        val authentication = SecurityContextHolder.getContext().authentication
        val userRoles = authentication.authorities.stream().map { r: GrantedAuthority? -> r!!.authority }.toList()
        val role = userRoles[0]

        if (role == UserRole.DRIVER.value) {
            return driverRepository.getDriverInfo(id)
        }
        if(role == UserRole.FRANCHISEE.value) {
            return franchiseeRepository.getFranchiseeInfo(id)
        }
        return UserResponseDto.of(userRepository.findById(id).orElseThrow())
    }

    fun passwordChange(id : Long, passwordChangeDto: PasswordChangeDto, isSuperAdmin : Boolean) {
        if (!isSuperAdmin) {
            if (!passwordCheck(id, passwordChangeDto.password!!)) {
                throw BaseException(ErrorCode.NOT_MATCH_PASSWORD)
            }
        }
        if (userRepository.modifyPassword(id, passwordEncoder.encode(passwordChangeDto.newPassword)) != 1) {
            throw BaseException(ErrorCode.INTERNAL_SERVER_ERROR)
        }
    }

    private fun passwordCheck(id: Long, password: String): Boolean {
        return passwordEncoder.matches(password, getUser(id).password)
    }

    fun existsByUsername(username : String) : Boolean{
        return userRepository.existsByUsername(username)
    }

    fun deleteUser(id: Long) {
        userRepository.deleteById(id)
    }

    @Transactional
    fun signupAdmin(adminSignupDto: AdminSignupDto): AdminResponseDto {
        //아이디 중복검사
        if (existsByUsername(adminSignupDto.username)) {
            throw BaseException(ErrorCode.DUPLICATE_USER_ID)
        }

        val adminGroupRequestDto = AdminGroupRequestDto(
            companyName = adminSignupDto.companyName
            , address = adminSignupDto.address
            , detailAddress = adminSignupDto.detailAddress
            , zipCode = adminSignupDto.zipCode
            , companyNumber = adminSignupDto.companyNumber
            , ceoName = adminSignupDto.ceoName
            , ceoPhone = adminSignupDto.ceoPhone
            , barobillId = adminSignupDto.barobillId
        )

//        //가맹점 그룹생성
        val adminGroup = adminGroupService.create(adminGroupRequestDto)

//        val adminGroup = adminGroupService.entity(adminSignupDto.adminGroupId)

        val userId = signup(
            username = adminSignupDto.username
            , password = adminSignupDto.password
            , role = UserRole.ADMIN
            , adminGroup = adminGroup
            , depositName = adminSignupDto.ceoName
        )

        return AdminResponseDto.of(
            adminRepository.save(adminSignupDto.toEntity(userId))
            , adminSignupDto.username
        )
    }

    fun getAdminEntity(id: Long): Admin = adminRepository.findById(id).orElseThrow {
        BaseException(
            ErrorCode.ENTITY_NOT_FOUND,
        )
    }

    fun signupFranchisee(franchiseeSignupDto: FranchiseeSignupDto, createUserId : Long): Long {
        //중복검사
        if (existsByUsername(franchiseeSignupDto.managerPhone)) {
            throw BaseException(ErrorCode.DUPLICATE_USER_ID)
        }

        val adminGroup = adminGroupService.entity(userRepository.getUserAdminGroupId(createUserId)!!)

        val userId = signup(
            username = franchiseeSignupDto.managerPhone
            , password = franchiseeSignupDto.code
            , role = UserRole.FRANCHISEE
            , adminGroup = adminGroup
            , depositName = franchiseeSignupDto.bankOwner
        )


        val franchisee = franchiseeRepository.save(franchiseeSignupDto.toEntity(userId, getUser(createUserId) ))
        return franchisee.id ?: throw BaseException(ErrorCode.INTERNAL_SERVER_ERROR)
    }

    fun signupCallWorker(callWorkerSignupDto: CallWorkerSignupDto, createUserId: Long): Long {
        if (existsByUsername(callWorkerSignupDto.username)) {
            throw BaseException(ErrorCode.DUPLICATE_USER_ID)
        }

        val adminGroup = adminGroupService.entity(userRepository.getUserAdminGroupId(createUserId)!!)

        val userId = signup(
            username = callWorkerSignupDto.username
            , password = callWorkerSignupDto.password
            , role = UserRole.CALL_WORKER
            , adminGroup = adminGroup
            , depositName = null
        )

        return callWorkerRepository.save(callWorkerSignupDto.toEntity(userId)).id ?: throw BaseException(ErrorCode.INTERNAL_SERVER_ERROR)
    }

    fun signupDriver(driverSignupDto: DriverSignupDto, createUserId: Long): Long {
        if (existsByUsername(driverSignupDto.phone)) {
            throw BaseException(ErrorCode.DUPLICATE_USER_ID)
        }

        val authentication = SecurityContextHolder.getContext().authentication
        val isSuperAdmin = Utils.roleCheck(authentication, listOf(UserRole.SUPER_ADMIN))
        val adminGroupId : Long? = if (isSuperAdmin) {
            driverSignupDto.adminGroupId
        } else {
            userRepository.getUserAdminGroupId(createUserId)
        }

        val userId = signup(
            driverSignupDto.phone, driverSignupDto.code, UserRole.DRIVER, adminGroupId?.let { adminGroupService.entity(it) }, driverSignupDto.bankOwner
        )

        val driver = driverRepository.save(driverSignupDto.toEntity(
            userId
            , getUser(createUserId)
            , carTypeService.detail(driverSignupDto.carTypeId)
        ))
        return driver.id ?: throw BaseException(ErrorCode.INTERNAL_SERVER_ERROR)
    }

    fun getCompanyDriverList(driverSearchDto: DriverSearchDto, paginateDto: PaginateDto, searchUserId: Long) : Page<CompanyDriverResponseDto>{
        return driverRepository.getCompanyDriverList(driverSearchDto, paginateDto.pageable(), searchUserId)
    }

    fun getDriverList(driverSearchDto: DriverSearchDto, paginateDto: PaginateDto): Page<DriverAllListResponseDto> {
        return driverRepository.getDriverList(driverSearchDto, paginateDto.pageable())
    }

    fun getDriverDetail(id: Long): CompanyDriverResponseDto = driverRepository.getDriverDetail(id)

    fun modifyDriver(id: Long, driverSignupDto: DriverSignupDto) {
        driverSignupDto.carType = carTypeService.detail(driverSignupDto.carTypeId)
        if (driverRepository.modifyDriver(id, driverSignupDto) != 1) {
            throw BaseException(ErrorCode.INTERNAL_SERVER_ERROR)
        }
    }

    fun getFranchiseeList(franchiseeSearchDto: FranchiseeSearchDto, paginateDto: PaginateDto) : Page<FranchiseSearchResponseDto>{
        return franchiseeRepository.getFranchiseeList(franchiseeSearchDto, paginateDto.pageable())
    }

    fun getDriverEntity(id: Long): Driver = driverRepository.findById(id).orElseThrow {
        BaseException(
            ErrorCode.ENTITY_NOT_FOUND,
        )
    }


    fun getDriverRedisSaveDto(driverId: Long): DriverRedisSaveDto {
        return driverRepository.getDriverRedisSaveDto(driverId)
    }

    fun getFranchiseeEntity(id: Long): Franchisee = franchiseeRepository.findById(id).orElseThrow {
        BaseException(
            ErrorCode.ENTITY_NOT_FOUND,
        )
    }

    fun getDriverControlDriverInfo(driverId: Long): DriverControlDriverInfoResponseDto {
        return driverRepository.getDriverControlDriverInfo(driverId)
    }

    fun getPaymentTargetUserIdList() = userRepository.getPaymentTargetUserIdList()

    fun getDriverCarTypeId(driverId: Long): Long {
        return driverRepository.getDriverCarTypeId(driverId)
    }

    fun getUserAdminGroupCode(userId: Long): String {
        return userRepository.getUserAdminGroupCode(userId) ?: throw BaseException(ErrorCode.ENTITY_NOT_FOUND)
    }

    fun getUserList(userSearchDto: UserSearchDto, paginateDto: PaginateDto): Page<UserListResponseDto> {
        return userRepository.getUserList(userSearchDto, paginateDto.pageable())
    }
}
