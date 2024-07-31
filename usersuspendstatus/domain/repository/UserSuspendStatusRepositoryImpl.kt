package com.ilogistic.delivery_admin_backend.usersuspendstatus.domain.repository

import com.ilogistic.delivery_admin_backend.usersuspendstatus.domain.dto.QUserSuspendStatusResponseDto
import com.ilogistic.delivery_admin_backend.usersuspendstatus.domain.dto.UserSuspendStatusResponseDto
import com.ilogistic.delivery_admin_backend.usersuspendstatus.domain.entity.QUserSuspendStatus.userSuspendStatus
import com.querydsl.jpa.impl.JPAQueryFactory
import org.springframework.stereotype.Repository

@Repository
class UserSuspendStatusRepositoryImpl(
    private val queryFactory: JPAQueryFactory
) : UserSuspendStatusRepositoryCustom{

    override fun getSuspendLastInfo(userId: Long): UserSuspendStatusResponseDto? {
        return queryFactory.select(
            QUserSuspendStatusResponseDto(
                userSuspendStatus.reason,
                userSuspendStatus.suspendType
            )
        ).from(userSuspendStatus)
            .where(userSuspendStatus.user.id.eq(userId))
            .orderBy(userSuspendStatus.id.desc())
            .fetchOne()
    }
}
