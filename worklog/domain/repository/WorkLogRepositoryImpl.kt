package com.ilogistic.delivery_admin_backend.worklog.domain.repository

import com.ilogistic.delivery_admin_backend.user.domain.entity.Driver
import com.ilogistic.delivery_admin_backend.worklog.domain.entity.QWorkLog.workLog
import com.ilogistic.delivery_admin_backend.worklog.enums.CommuteType
import com.querydsl.jpa.impl.JPAQueryFactory
import org.springframework.stereotype.Repository

@Repository
class WorkLogRepositoryImpl(
    private val queryFactory: JPAQueryFactory
) : WorkLogRepositoryCustom{

    override fun getLastWorkType(driver: Driver): CommuteType {
        return queryFactory.select(workLog.type)
            .from(workLog)
            .where(workLog.driver.eq(driver))
            .orderBy(workLog.id.desc())
            .limit(1)
            .fetchOne() ?: CommuteType.OFF
    }
}
