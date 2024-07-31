package com.ilogistic.delivery_admin_backend.userpoint.domain.repository

import com.ilogistic.delivery_admin_backend.userpoint.domain.entity.QUserPoint
import com.querydsl.jpa.impl.JPAQueryFactory
import org.springframework.stereotype.Repository

@Repository
class UserPointRepositoryImpl(
    private val queryFactory: JPAQueryFactory
) : UserPointRepositoryCustom {

    override fun getUserPoint(userId: Long): Int? {
        val userPoint = QUserPoint.userPoint
        return queryFactory.select(userPoint.currentPoint)
            .from(userPoint)
            .where(userPoint.user.id.eq(userId))
            .orderBy(userPoint.id.desc())
            .fetchFirst()
    }
}
