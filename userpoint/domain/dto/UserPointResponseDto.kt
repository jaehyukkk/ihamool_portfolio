package com.ilogistic.delivery_admin_backend.userpoint.domain.dto

import com.ilogistic.delivery_admin_backend.userpoint.domain.entity.UserPoint
import com.ilogistic.delivery_admin_backend.userpoint.enums.PointReason
import com.ilogistic.delivery_admin_backend.userpoint.enums.PointType
import java.time.LocalDateTime

data class UserPointResponseDto(
    val id: Long? = null,
    var currentPoint: Int? = 0,
    var chargedPoint: Int? = 0,
    var deductedPoint: Int? = 0,
    var pointReason: PointReason,
    var pointType: PointType,
    val memo: String? = null,
    val createdDate: LocalDateTime,
) {

    companion object{
        fun of(userPoint: UserPoint) = UserPointResponseDto(
            id = userPoint.id,
            currentPoint = userPoint.currentPoint,
            chargedPoint = userPoint.chargedPoint,
            deductedPoint = userPoint.deductedPoint,
            pointReason = userPoint.pointReason,
            pointType = userPoint.pointType,
            memo = userPoint.memo,
            createdDate = userPoint.createdDate
        )
    }
}
