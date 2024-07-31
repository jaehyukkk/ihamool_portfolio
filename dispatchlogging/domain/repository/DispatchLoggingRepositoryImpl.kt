package com.ilogistic.delivery_admin_backend.dispatchlogging.domain.repository

import com.ilogistic.delivery_admin_backend.dispatch.enums.DispatchStatus
import com.ilogistic.delivery_admin_backend.dispatchlogging.domain.dto.DispatchLoggingResponseDto
import com.ilogistic.delivery_admin_backend.dispatchlogging.domain.dto.QDispatchLoggingResponseDto
import com.ilogistic.delivery_admin_backend.dispatchlogging.domain.entity.QDispatchLogging.dispatchLogging
import com.ilogistic.delivery_admin_backend.user.domain.entity.QDriver.driver
import com.querydsl.jpa.impl.JPAQueryFactory
import org.springframework.stereotype.Repository

@Repository
class DispatchLoggingRepositoryImpl(
    private val queryFactory: JPAQueryFactory
) : DispatchLoggingRepositoryCustom{

    override fun getDriverCancelCount(driverId: Long, dispatchId: Long, status: DispatchStatus): Long? {
        return queryFactory.select(dispatchLogging.id.count())
            .from(dispatchLogging)
            .where(dispatchLogging.driverId.eq(driverId)
                .and(dispatchLogging.status.eq(status))
                .and(dispatchLogging.dispatchId.eq(dispatchId)))
            .fetchOne()
    }

    override fun getLoggingList(dispatchId: Long): List<DispatchLoggingResponseDto> {
        return queryFactory.select(
            QDispatchLoggingResponseDto(
            dispatchLogging.id!!,
            driver.id,
                driver.name,
                driver.phone,
                driver.carNumber,
            dispatchLogging.status,
            dispatchLogging.isForce,
            dispatchLogging.createdDate
        )
        )
            .from(dispatchLogging)
            .leftJoin(driver).on(dispatchLogging.driverId.eq(driver.id))
            .where(dispatchLogging.dispatchId.eq(dispatchId))
            .fetch()
    }
}
