package com.ilogistic.delivery_admin_backend.user.repository.driver

import com.ilogistic.delivery_admin_backend.admingroup.domain.entity.QAdminGroup.adminGroup
import com.ilogistic.delivery_admin_backend.cartype.domain.entity.QCarType.carType
import com.ilogistic.delivery_admin_backend.dispatch.domain.entity.QDispatch.dispatch
import com.ilogistic.delivery_admin_backend.dispatch.enums.DispatchStatus
import com.ilogistic.delivery_admin_backend.exception.BaseException
import com.ilogistic.delivery_admin_backend.exception.CustomRuntimeException
import com.ilogistic.delivery_admin_backend.exception.ErrorCode
import com.ilogistic.delivery_admin_backend.user.domain.dto.*
import com.ilogistic.delivery_admin_backend.user.domain.entity.QDriver.driver
import com.ilogistic.delivery_admin_backend.user.domain.entity.QUser
import com.ilogistic.delivery_admin_backend.user.domain.entity.QUser.user
import com.ilogistic.delivery_admin_backend.worklog.domain.entity.QWorkLog.workLog
import com.querydsl.core.types.dsl.BooleanExpression
import com.querydsl.jpa.JPAExpressions
import com.querydsl.jpa.impl.JPAQueryFactory
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.support.PageableExecutionUtils
import org.springframework.stereotype.Repository

@Repository
class DriverRepositoryImpl(
    private val queryFactory: JPAQueryFactory
) : DriverRepositoryCustom{

    private fun driverDefaultSelect() = QCompanyDriverResponseDto(
        driver.id,
        driver.name,
        driver.phone,
        driver.bankNumber,
        driver.bank,
        driver.bankOwner,
        driver.address,
        driver.detailAddress,
        driver.zipCode,
        driver.code,
        driver.carNumber,
        driver.carType.id,
        driver.carType.name,
        JPAExpressions.select(
            carType.id
        ).from(carType)
            .where(carType.id.eq(driver.carType.parentId)),
        JPAExpressions.select(
            carType.name
        ).from(carType)
            .where(carType.id.eq(driver.carType.parentId)),
        driver.companyNumber,
        driver.companyName,
        driver.companyAddress,
        driver.companyDetailAddress
    )

    override fun getCompanyDriverList(driverSearchDto: DriverSearchDto, pageable: Pageable, searchUserId : Long): Page<CompanyDriverResponseDto> {
        val user1 = QUser("user1")
        val query = queryFactory.select(
            driverDefaultSelect()
        ).from(driver)
            .leftJoin(driver.carType)
            .leftJoin(user).on(driver.id.eq(user.id))
            .where(
                user.adminGroup.id.eq(
                    JPAExpressions.select(user1.adminGroup.id)
                        .from(user1)
                        .where(user1.id.eq(searchUserId))
                ),
                search(driverSearchDto))
            .orderBy(driver.id.desc())
            .offset(pageable.offset)
            .limit(pageable.pageSize.toLong())

        val count = queryFactory.select(driver.id.count())
            .from(driver)
            .where(search(driverSearchDto))
            .fetchFirst()

        return PageableExecutionUtils.getPage(query.fetch(), pageable) {count}
    }

    override fun getDriverList(
        driverSearchDto: DriverSearchDto,
        pageable: Pageable,
    ): Page<DriverAllListResponseDto> {
        val query = queryFactory.select(
            QDriverAllListResponseDto(
                driver.id,
                driver.name,
                driver.phone,
                driver.bankNumber,
                driver.bank,
                driver.bankOwner,
                driver.address,
                driver.detailAddress,
                driver.zipCode,
                driver.code,
                driver.carNumber,
                driver.carType.id,
                driver.carType.name,
                JPAExpressions.select(
                    carType.id
                ).from(carType)
                    .where(carType.id.eq(driver.carType.parentId)),
                JPAExpressions.select(
                    carType.name
                ).from(carType)
                    .where(carType.id.eq(driver.carType.parentId)),
                driver.companyNumber,
                driver.companyName,
                driver.companyAddress,
                driver.companyDetailAddress,
                adminGroup.id,
                adminGroup.companyName
            )
        ).from(driver)
            .leftJoin(driver.carType)
            .leftJoin(user).on(driver.id.eq(user.id))
            .leftJoin(user.adminGroup, adminGroup)
            .where(
                search(driverSearchDto))
            .orderBy(driver.id.desc())
            .offset(pageable.offset)
            .limit(pageable.pageSize.toLong())

        val count = queryFactory.select(driver.id.count())
            .from(driver)
            .where(search(driverSearchDto))
            .fetchFirst()

        return PageableExecutionUtils.getPage(query.fetch(), pageable) {count}
    }

    override fun getDriverDetail(driverId: Long): CompanyDriverResponseDto {
        return queryFactory.select(
            driverDefaultSelect()
        ).from(driver)
            .leftJoin(driver.carType)
            .where(driver.id.eq(driverId))
            .fetchOne() ?: throw BaseException(ErrorCode.ENTITY_NOT_FOUND)
    }

    override fun getDriverInfo(driverId: Long): DriverInfoResponseDto {
        return queryFactory.select(
            QDriverInfoResponseDto(
                driver.id,
                user.userRole,
                user.username,
                driver.name,
                driver.phone,
                driver.bankNumber,
                driver.bank,
                driver.bankOwner,
                driver.address,
                driver.detailAddress,
                driver.zipCode,
                driver.code,
                driver.carNumber,
                adminGroup.groupCode,
                JPAExpressions.select(
                    workLog.type
                ).from(workLog)
                    .where(workLog.id.eq(
                        JPAExpressions.select(workLog.id.max())
                            .from(workLog)
                            .where(workLog.driver.id.eq(driver.id))
                    ))

            )
        ).from(driver)
            .leftJoin(user).on(driver.id.eq(user.id))
            .leftJoin(user.adminGroup, adminGroup)
            .where(driver.id.eq(driverId))
        .fetchOne() ?: throw BaseException(ErrorCode.ENTITY_NOT_FOUND)
    }

    override fun getDriverControlDriverInfo(driverId: Long): DriverControlDriverInfoResponseDto {
        return queryFactory.select(
            QDriverControlDriverInfoResponseDto(
                driver.id,
                driver.name,
                driver.phone,
                driver.carNumber,
                JPAExpressions.select(dispatch.id.count())
                    .from(dispatch)
                    .where(dispatch.driver.id.eq(driver.id).and(dispatch.status.eq(DispatchStatus.DISPATCHED))),
                JPAExpressions.select(dispatch.id.count())
                    .from(dispatch)
                    .where(dispatch.driver.id.eq(driver.id).and(dispatch.status.eq(DispatchStatus.LOADING))))
            ).from(driver)
            .where(driver.id.eq(driverId))
            .fetchOne() ?: throw BaseException(ErrorCode.ENTITY_NOT_FOUND)
    }

    override fun getDriverRedisSaveDto(driverId: Long): DriverRedisSaveDto {
        return queryFactory.select(
            QDriverRedisSaveDto(
                driver.id,
                driver.name,
                driver.phone,
                driver.carNumber,
                driver.carType.id
            )
        ).from(driver)
            .where(driver.id.eq(driverId))
            .join(driver.carType)
            .fetchOne() ?: throw BaseException(ErrorCode.ENTITY_NOT_FOUND)
    }

    override fun getDriverCarTypeId(driverId: Long): Long {
        return queryFactory.select(driver.carType.id)
            .from(driver)
            .where(driver.id.eq(driverId))
            .fetchOne() ?: throw BaseException(ErrorCode.ENTITY_NOT_FOUND)
    }

    private fun search(driverSearchDto: DriverSearchDto) : BooleanExpression? {
        if (driverSearchDto.search.isNullOrBlank() || driverSearchDto.searchType.isNullOrBlank()) {
//            throw CustomRuntimeException(ErrorCode.BAD_REQUEST)
            return null
        }
        return when(driverSearchDto.searchType) {
            "1" -> driver.carNumber.contains(driverSearchDto.search)
            "2" -> driver.name.contains(driverSearchDto.search)
            "3" -> driver.phone.contains(driverSearchDto.search)
            "4" -> driver.carNumber.contains(driverSearchDto.search)
                .or(driver.name.contains(driverSearchDto.search))
                .or(driver.phone.contains(driverSearchDto.search))
            else -> throw CustomRuntimeException(ErrorCode.BAD_REQUEST)
        }
    }
}
