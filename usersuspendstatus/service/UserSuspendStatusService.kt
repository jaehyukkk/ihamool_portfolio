package com.ilogistic.delivery_admin_backend.usersuspendstatus.service

import com.ilogistic.delivery_admin_backend.exception.BaseException
import com.ilogistic.delivery_admin_backend.exception.ErrorCode
import com.ilogistic.delivery_admin_backend.user.service.UserService
import com.ilogistic.delivery_admin_backend.usersuspendstatus.domain.dto.UserSuspendStatusRequestDto
import com.ilogistic.delivery_admin_backend.usersuspendstatus.domain.dto.UserSuspendStatusResponseDto
import com.ilogistic.delivery_admin_backend.usersuspendstatus.domain.entity.UserSuspendStatus
import com.ilogistic.delivery_admin_backend.usersuspendstatus.domain.repository.UserSuspendStatusRepository
import com.ilogistic.delivery_admin_backend.usersuspendstatus.enums.SuspendType
import org.springframework.stereotype.Service

@Service
class UserSuspendStatusService(
    private val userSuspendStatusRepository: UserSuspendStatusRepository,
    private val userService: UserService
) {
    fun create(userSuspendStatusRequestDto: UserSuspendStatusRequestDto) {
        val user = userSuspendStatusRequestDto.user ?: userService.getUser(
            userSuspendStatusRequestDto.userId ?: throw BaseException(ErrorCode.BAD_REQUEST)
        )

        userSuspendStatusRepository.save(userSuspendStatusRequestDto.toEntity(user))
    }

    fun getSuspendLastInfo(userId: Long): UserSuspendStatusResponseDto? {
        return userSuspendStatusRepository.getSuspendLastInfo(userId)
    }

}
