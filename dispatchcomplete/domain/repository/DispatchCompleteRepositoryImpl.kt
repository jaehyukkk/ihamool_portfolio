package com.ilogistic.delivery_admin_backend.dispatchcomplete.domain.repository

import com.ilogistic.delivery_admin_backend.admingroup.domain.entity.QAdminGroup.adminGroup
import com.ilogistic.delivery_admin_backend.api.domain.dto.QTaxInvoiceRequestDto
import com.ilogistic.delivery_admin_backend.api.domain.dto.TaxInvoiceRequestDto
import com.ilogistic.delivery_admin_backend.dispatch.domain.entity.QDispatch.dispatch
import com.ilogistic.delivery_admin_backend.dispatchcomplete.domain.dto.*
import com.ilogistic.delivery_admin_backend.dispatchcomplete.domain.entity.QDispatchComplete.dispatchComplete
import com.ilogistic.delivery_admin_backend.exception.BaseException
import com.ilogistic.delivery_admin_backend.exception.ErrorCode
import com.ilogistic.delivery_admin_backend.user.domain.entity.QDriver.driver
import com.ilogistic.delivery_admin_backend.user.domain.entity.QUser
import com.ilogistic.delivery_admin_backend.user.domain.entity.QUser.user
import com.ilogistic.delivery_admin_backend.utils.QuerydslUtil
import com.ilogistic.delivery_admin_backend.worklog.domain.entity.QWorkLog.workLog
import com.ilogistic.delivery_admin_backend.worklog.enums.CommuteType
import com.querydsl.core.types.ExpressionUtils
import com.querydsl.core.types.Order
import com.querydsl.core.types.OrderSpecifier
import com.querydsl.core.types.Projections
import com.querydsl.core.types.dsl.BooleanExpression
import com.querydsl.core.types.dsl.Expressions
import com.querydsl.core.types.dsl.StringPath
import com.querydsl.jpa.JPAExpressions
import com.querydsl.jpa.impl.JPAQuery
import com.querydsl.jpa.impl.JPAQueryFactory
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.support.PageableExecutionUtils
import org.springframework.stereotype.Repository
import java.time.LocalDateTime

@Repository
class DispatchCompleteRepositoryImpl(
    private val queryFactory: JPAQueryFactory
) : DispatchCompleteRepositoryCustom{

    override fun dispatchCompleteStatistics(driverId: Long): DispatchCompleteStatisticsResponseDto? {
        return queryFactory.select(
            QDispatchCompleteStatisticsResponseDto(
                dispatchComplete.id.count(),
                dispatchComplete.dispatch.originalPrice.sum(),
                dispatchComplete.dispatch.driverPrice.sum(),
                dispatchComplete.dispatch.distance.sum()
            )
        ).from(dispatchComplete)
            .leftJoin(dispatchComplete.dispatch)
            .leftJoin(dispatchComplete.driver)
            .where(dispatchComplete.driver.id.eq(driverId),
                dispatchComplete.createdDate.after(
                    JPAExpressions.select(workLog.createdDate.max())
                        .from(workLog)
                        .where(workLog.driver.id.eq(driverId), workLog.type.eq(CommuteType.ON))
                )
            )
            .groupBy(dispatchComplete.driver.id)
            .fetchOne()
    }

    override fun getCompanyTotalStatistics(statisticsSearchDto: StatisticsSearchDto, adminId: Long): CompanyTotalStatisticsResponseDto? {

        val user1 = QUser("user")
        return queryFactory.select(
            QCompanyTotalStatisticsResponseDto(
                dispatchComplete.id.count(),
                dispatchComplete.dispatch.originalPrice.sum(),
//                (dispatchComplete.dispatch.originalPrice.subtract(dispatchComplete.dispatch.driverPrice).subtract(dispatchComplete.dispatch.shipPrice)).sum()
                dispatchComplete.dispatch.originalPrice.multiply(dispatchComplete.dispatch.fee.divide(100)).sum(),
            )
        ).from(dispatchComplete)
            .leftJoin(dispatchComplete.dispatch, dispatch)
            .leftJoin(dispatch.finalApprovalUser, user)
            .where(
                user.adminGroup.id.eq(
                    JPAExpressions.select(user1.adminGroup.id)
                        .from(user1)
                        .where(user1.id.eq(adminId))
                ),
                QuerydslUtil.betweenDate(
                dispatchComplete.createdDate,
                statisticsSearchDto.startDate,
                statisticsSearchDto.endDate
            ))
            .fetchOne()
    }

    private fun getStatisticsQuery() : JPAQuery<StatisticsResponseDto> {
        return queryFactory.select(
            QStatisticsResponseDto(
                dispatchComplete.id.count(),
                dispatchComplete.dispatch.originalPrice.sum(),
                dispatchComplete.dispatch.originalPrice.multiply(dispatchComplete.dispatch.fee.divide(100)).sum(),
                Expressions.stringTemplate("DATE_FORMAT({0},{1})", dispatchComplete.createdDate,
                    "%Y%m%d"),
            )
        ).from(dispatchComplete)
            .leftJoin(dispatchComplete.dispatch, dispatch)
            .leftJoin(dispatch.finalApprovalUser, user)
            .groupBy(Expressions.stringTemplate("DATE_FORMAT({0},{1})", dispatchComplete.createdDate, "%Y%m%d"))
    }
    override fun getCompanyStatistics(statisticsSearchDto: StatisticsSearchDto, pageable: Pageable, adminId: Long): Page<StatisticsResponseDto> {

        val user1 = QUser("user")
        val query = getStatisticsQuery()
            .where(
                user.adminGroup.id.eq(
                    JPAExpressions.select(user1.adminGroup.id)
                        .from(user1)
                        .where(user1.id.eq(adminId))
                ),
                QuerydslUtil.betweenDate(
                dispatchComplete.createdDate,
                statisticsSearchDto.startDate,
                statisticsSearchDto.endDate
            ))
            .orderBy(dispatchComplete.createdDate.desc())
            .offset(pageable.offset)
            .limit(pageable.pageSize.toLong())
            .fetch()

        val count = queryFactory.select(
            dispatchComplete.id.count()
        ).from(dispatchComplete)
            .leftJoin(dispatchComplete.dispatch, dispatch)
            .leftJoin(dispatch.finalApprovalUser, user)
            .where(
                user.adminGroup.id.eq(
                    JPAExpressions.select(user1.adminGroup.id)
                        .from(user1)
                        .where(user1.id.eq(adminId))
                ),
                QuerydslUtil.betweenDate(
                dispatchComplete.createdDate,
                statisticsSearchDto.startDate,
                statisticsSearchDto.endDate
            ))
            .groupBy(Expressions.stringTemplate("DATE_FORMAT({0},{1})", dispatchComplete.createdDate, "%Y%m%d"))
            .fetchFirst()

        return PageableExecutionUtils.getPage(query, pageable) {count}
    }

    override fun getCompanyStatisticsList(
        statisticsSearchDto: StatisticsSearchDto,
        userId: Long
    ): List<StatisticsResponseDto> {
        val user1 = QUser("user")
        return getStatisticsQuery()
            .where(
                user.adminGroup.id.eq(
                    JPAExpressions.select(user1.adminGroup.id)
                        .from(user1)
                        .where(user1.id.eq(userId))
                ),
                QuerydslUtil.betweenDate(
                    dispatchComplete.createdDate,
                    statisticsSearchDto.startDate,
                    statisticsSearchDto.endDate
                ))
            .orderBy(dispatchComplete.createdDate.desc())
            .fetch()
    }

    override fun getDriverSelfStatistics(
        driverId: Long,
        statisticsSearchDto: StatisticsSearchDto
    ): List<DriverSelfStatisticsListResponseDto>? {

        return queryFactory.select(
            QDriverSelfStatisticsListResponseDto(
                dispatchComplete.id.count(),
                dispatchComplete.dispatch.driverPrice.sum(),
                Expressions.stringTemplate("DATE_FORMAT({0},{1})", dispatchComplete.createdDate,
                    "%Y%m%d"),
            )
        ).from(dispatchComplete)
            .leftJoin(dispatchComplete.driver)
            .leftJoin(dispatchComplete.dispatch)
            .where(QuerydslUtil.betweenDate(
                dispatchComplete.createdDate,
                statisticsSearchDto.startDate,
                statisticsSearchDto.endDate
            ), dispatchComplete.driver.id.eq(driverId))
            .orderBy(dispatchComplete.createdDate.desc())
            .groupBy(Expressions.stringTemplate("DATE_FORMAT({0},{1})", dispatchComplete.createdDate, "%Y%m%d"))
            .fetch()
    }

    override fun getDriverDailyStatisticsListResponseDto(
        driverId: Long,
        statisticsSearchDto: StatisticsSearchDto
    ): List<DriverDailyStatisticsListResponseDto> {
        return queryFactory.select(
            QDriverDailyStatisticsListResponseDto(
                dispatchComplete.id,
                dispatchComplete.dispatch.id,
                dispatchComplete.dispatch.startSigungu,
                dispatchComplete.dispatch.startSido,
                dispatchComplete.dispatch.endSigungu,
                dispatchComplete.dispatch.endSido,
                dispatchComplete.dispatch.distance,
                dispatchComplete.dispatch.fee,
                dispatchComplete.dispatch.originalPrice,
                dispatchComplete.dispatch.originalPrice.multiply(dispatchComplete.dispatch.fee.divide(100)),
                dispatchComplete.dispatch.driverPrice,
                dispatchComplete.dispatch.shipPrice,
                dispatchComplete.createdDate
            )
        ).from(dispatchComplete)
            .leftJoin(dispatchComplete.dispatch)
            .leftJoin(dispatchComplete.driver)
            .where(QuerydslUtil.betweenDate(
                dispatchComplete.createdDate,
                statisticsSearchDto.startDate,
                statisticsSearchDto.endDate
            ), dispatchComplete.driver.id.eq(driverId))
            .orderBy(dispatchComplete.createdDate.desc())
            .fetch()
    }

    override fun getDriverStatistics(
        driverStatisticsSearchDto: DriverStatisticsSearchDto,
        pageable: Pageable,
        adminId: Long
    ): Page<DriverStatisticsResponseDto> {
        val driverPrice = Expressions.stringPath("driverPrice")
        val user1 = QUser("user")
        val query = queryFactory.select(
            QDriverStatisticsResponseDto(
                dispatchComplete.driver.name,
                dispatchComplete.driver.phone,
                dispatchComplete.driver.carNumber,
                dispatchComplete.dispatch.originalPrice.sum().coalesce(0),
                ExpressionUtils.`as`(
                    dispatchComplete.dispatch.driverPrice.sum().coalesce(0),
                    "driverPrice"
                ),
                dispatchComplete.dispatch.shipPrice.sum().coalesce(0),
                dispatchComplete.dispatch.originalPrice.multiply(dispatchComplete.dispatch.fee.divide(100)).sum().coalesce(0),
                dispatchComplete.id.count().coalesce(0),
            )
        ).from(dispatchComplete)
            .leftJoin(dispatchComplete.driver, driver)
            .leftJoin(dispatchComplete.dispatch, dispatch)
            .leftJoin(user).on(user.id.eq(driver.id))
            .where(
                user.adminGroup.id.eq(
                    JPAExpressions.select(user1.adminGroup.id)
                        .from(user1)
                        .where(user1.id.eq(adminId))
                ),
                QuerydslUtil.betweenDate(
                dispatchComplete.createdDate,
                driverStatisticsSearchDto.startDate,
                driverStatisticsSearchDto.endDate
            ), search(driverStatisticsSearchDto))
            .offset(pageable.offset)
            .limit(pageable.pageSize.toLong())
            .orderBy(*dynamicSort(driverStatisticsSearchDto, driverPrice).toTypedArray())
            .groupBy(dispatchComplete.driver.id)
            .fetch()

        val count = queryFactory.select(dispatchComplete.id.count())
            .from(dispatchComplete)
            .leftJoin(dispatchComplete.driver, driver)
            .leftJoin(user).on(user.id.eq(driver.id))
            .where(
                user.adminGroup.id.eq(
                    JPAExpressions.select(user1.adminGroup.id)
                        .from(user1)
                        .where(user1.id.eq(adminId))
                ),
                QuerydslUtil.betweenDate(
                dispatchComplete.createdDate,
                driverStatisticsSearchDto.startDate,
                driverStatisticsSearchDto.endDate
            ), search(driverStatisticsSearchDto))
            .fetchFirst()

        return PageableExecutionUtils.getPage(query, pageable) {count}
    }

    override fun getDriverStatisticsList(
        driverStatisticsSearchDto: DriverStatisticsSearchDto,
        userId: Long
    ): List<DriverStatisticsResponseDto> {
        val driverPrice = Expressions.stringPath("driverPrice")
        val user1 = QUser("user")
        return queryFactory.select(
            QDriverStatisticsResponseDto(
                dispatchComplete.driver.name,
                dispatchComplete.driver.phone,
                dispatchComplete.driver.carNumber,
                dispatchComplete.dispatch.originalPrice.sum().coalesce(0),
                ExpressionUtils.`as`(
                    dispatchComplete.dispatch.driverPrice.sum().coalesce(0),
                    "driverPrice"
                ),
                dispatchComplete.dispatch.shipPrice.sum().coalesce(0),
                dispatchComplete.dispatch.originalPrice.multiply(dispatchComplete.dispatch.fee.divide(100)).sum().coalesce(0),
                dispatchComplete.id.count().coalesce(0),
            )
        ).from(dispatchComplete)
            .leftJoin(dispatchComplete.driver, driver)
            .leftJoin(dispatchComplete.dispatch, dispatch)
            .leftJoin(user).on(user.id.eq(driver.id))
            .where(
                user.adminGroup.id.eq(
                    JPAExpressions.select(user1.adminGroup.id)
                        .from(user1)
                        .where(user1.id.eq(userId))
                ),
                QuerydslUtil.betweenDate(
                    dispatchComplete.createdDate,
                    driverStatisticsSearchDto.startDate,
                    driverStatisticsSearchDto.endDate
                ), search(driverStatisticsSearchDto))
            .orderBy(*dynamicSort(driverStatisticsSearchDto, driverPrice).toTypedArray())
            .groupBy(dispatchComplete.driver.id)
            .fetch()
    }

    override fun getDriverCompleteScreenList(driverId: Long): List<DriverCompleteScreenListResponseDto> {
        return queryFactory.select(QDriverCompleteScreenListResponseDto(
            dispatchComplete.id,
            dispatchComplete.dispatch.id,
            dispatchComplete.dispatch.originalPrice,
            dispatchComplete.dispatch.driverPrice,
            dispatchComplete.createdDate,
            dispatch.loadingDateTime,
            dispatch.dispatchDateTime,
            dispatch.endAddress,
            dispatch.endDetailAddress,
            dispatch.distance,
            dispatchComplete.isTaxInvoice
        )).from(dispatchComplete)
            .leftJoin(dispatchComplete.dispatch, dispatch)
            .leftJoin(dispatchComplete.driver, driver)
            .where(
                dispatchComplete.driver.id.eq(driverId),
                //출근시간 다음꺼부터 가져오기
                dispatchComplete.createdDate.after(
                    JPAExpressions.select(workLog.createdDate.max())
                        .from(workLog)
                        .where(workLog.driver.id.eq(driverId), workLog.type.eq(CommuteType.ON))
                )
            )
            .orderBy(dispatchComplete.id.desc())
            .fetch()
    }

    override fun getDriverTotalSelfStatistics(driverId: Long,
                                              statisticsSearchDto: StatisticsSearchDto
                                              ): DriverTotalStatisticsSelfResponseDto? {

//        println("start date : ${statisticsSearchDto.startDate}")
//        println("end date : ${statisticsSearchDto.endDate}")
//        println("driverId : $driverId")

        return queryFactory.select(
            QDriverTotalStatisticsSelfResponseDto(
                dispatchComplete.dispatch.originalPrice.sum().coalesce(0),
                dispatchComplete.dispatch.driverPrice.sum().coalesce(0),
                dispatchComplete.dispatch.shipPrice.sum().coalesce(0),
                dispatchComplete.dispatch.originalPrice.multiply(dispatchComplete.dispatch.fee.divide(100)).sum().coalesce(0),
                dispatchComplete.id.count().coalesce(0)
            )
        ).from(dispatchComplete)
            .leftJoin(dispatchComplete.dispatch)
            .leftJoin(dispatchComplete.driver)
            .where(
                dispatchComplete.driver.id.eq(driverId),
//                dispatchComplete.createdDate.after(
//                    JPAExpressions.select(workLog.createdDate.max())
//                        .from(workLog)
//                        .where(workLog.driver.id.eq(driverId), workLog.type.eq(CommuteType.ON))
//                )
             QuerydslUtil.betweenDate(
                dispatchComplete.createdDate,
                    statisticsSearchDto.startDate,
                    statisticsSearchDto.endDate
            ))
            .fetchOne()
    }

    override fun getDispatchCompleteAmount(dispatchId: Long, userId: Long): DispatchCompleteAmountResponseDto? {
        return queryFactory.select(
            Projections.constructor(
                DispatchCompleteAmountResponseDto::class.java,
                dispatchComplete.id,
                dispatchComplete.dispatch.driverPrice,
            )
        ).from(dispatchComplete)
            .leftJoin(dispatchComplete.dispatch)
            .where(
                dispatchComplete.id.eq(dispatchId),
                dispatchComplete.driver.id.eq(userId)
            )
            .fetchFirst()
    }

    override fun getTaxInvoiceInfo(dispatchId: Long, userId: Long): TaxInvoiceRequestDto? {
        return queryFactory.select(
            QTaxInvoiceRequestDto(
                dispatchComplete.id,
                dispatchComplete.dispatch.driverPrice,
                driver.companyName,
                driver.companyNumber,
                driver.name,
                driver.address,
                driver.detailAddress,
                adminGroup.companyName,
                adminGroup.companyNumber,
                adminGroup.ceoName,
                adminGroup.address,
                adminGroup.detailAddress,
                adminGroup.barobillId
            )
        ).from(dispatchComplete)
            .leftJoin(dispatchComplete.dispatch, dispatch)
            .leftJoin(dispatchComplete.driver, driver)
            .leftJoin(dispatch.adminGroup, adminGroup)
            .where(
                dispatchComplete.id.eq(dispatchId),
                dispatchComplete.driver.id.eq(userId)
            )
            .fetchFirst()
    }

    private fun search(driverStatisticsSearchDto: DriverStatisticsSearchDto) : BooleanExpression? {
        if (!driverStatisticsSearchDto.searchType.isNullOrBlank()) {
            return when (driverStatisticsSearchDto.searchType) {
                "1" -> dispatchComplete.driver.name.contains(driverStatisticsSearchDto.search)
                "2" -> dispatchComplete.driver.carNumber.contains(driverStatisticsSearchDto.search)
                else -> throw BaseException(ErrorCode.BAD_REQUEST)
            }
        }
        return null
    }


    fun dynamicSort(driverStatisticsSearchDto: DriverStatisticsSearchDto, driverPrice: StringPath): List<OrderSpecifier<*>> {
        val orderList = mutableListOf<OrderSpecifier<*>>()
        if (!driverStatisticsSearchDto.sortType.isNullOrBlank()) {
            when (driverStatisticsSearchDto.sortType) {
                "1" -> orderList.add(OrderSpecifier(Order.DESC, driverPrice))
                "2" -> orderList.add(OrderSpecifier(Order.ASC, driverPrice))
            }
        }
        orderList.add(OrderSpecifier(Order.DESC, dispatchComplete.id))
        return orderList
    }
}
