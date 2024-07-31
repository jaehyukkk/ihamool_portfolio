package com.ilogistic.delivery_admin_backend.userpoint.service

import com.ilogistic.delivery_admin_backend.exception.BaseException
import com.ilogistic.delivery_admin_backend.exception.ErrorCode
import com.ilogistic.delivery_admin_backend.user.domain.entity.User
import com.ilogistic.delivery_admin_backend.user.service.UserService
import com.ilogistic.delivery_admin_backend.userpoint.domain.dto.UserPointRequestDto
import com.ilogistic.delivery_admin_backend.userpoint.domain.dto.UserPointResponseDto
import com.ilogistic.delivery_admin_backend.userpoint.domain.entity.UserPoint
import com.ilogistic.delivery_admin_backend.userpoint.domain.repository.UserPointRepository
import com.ilogistic.delivery_admin_backend.userpoint.enums.PointReason
import com.ilogistic.delivery_admin_backend.userpoint.enums.PointType
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class UserPointService(
    private val userPointRepository: UserPointRepository,
    private val userService: UserService
) {

    @Transactional
    fun chargePoint(userPointRequestDto: UserPointRequestDto): UserPoint {
        val user = userService.getUser(userPointRequestDto.userId)
        val currentPoint = getUserPoint(user.id!!)


        val point = userPointRequestDto.point

        val userPoint = UserPoint(
            user = user,
            currentPoint = currentPoint + point,
            chargedPoint = point,
            pointReason = userPointRequestDto.reason ?: PointReason.ADMIN_CHARGE,
            pointType = PointType.CHARGE,
            memo = userPointRequestDto.memo
        )
        return userPointRepository.save(userPoint)
    }

    @Transactional
    fun deductPoint(userPointRequestDto: UserPointRequestDto): UserPoint {
        val user = userService.getUser(userPointRequestDto.userId)
        val currentPoint = userPointRepository.findTopByUserOrderByIdDesc(user)?.currentPoint ?: 0
        if(currentPoint < userPointRequestDto.point) {
            throw BaseException(ErrorCode.POINT_NOT_ENOUGH)
        }
        val userPoint = UserPoint(
            user = user,
            currentPoint = currentPoint - userPointRequestDto.point,
            deductedPoint = userPointRequestDto.point,
            pointReason = userPointRequestDto.reason ?: PointReason.ADMIN_DEDUCT,
            pointType = PointType.DEDUCT,
        )
        return userPointRepository.save(userPoint)
    }

    fun getUserPoint(userId: Long) = userPointRepository.getUserPoint(userId) ?: 0

//    fun getUserPoint(user : User) = userPointRepository.findTopByUserOrderByIdDesc(user)?.currentPoint ?: 0

//    fun getUserPoint(id: Long) = userPointRepository.findTopByUserOrderByIdDesc(userService.getUser(id))?.currentPoint ?: 0

    fun getUserPointHistory(id: Long) = userPointRepository.findByUserOrderByIdDesc(userService.getUser(id)).map {
        UserPointResponseDto.of(it)
    }
}
