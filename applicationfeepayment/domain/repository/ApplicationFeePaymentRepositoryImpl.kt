package com.ilogistic.delivery_admin_backend.applicationfeepayment.domain.repository

import com.ilogistic.delivery_admin_backend.applicationfeepayment.domain.dto.*
import com.ilogistic.delivery_admin_backend.applicationfeepayment.domain.entity.QApplicationFeePayment.applicationFeePayment
import com.ilogistic.delivery_admin_backend.applicationfeepayment.enums.PaymentStatus
import com.ilogistic.delivery_admin_backend.user.enums.UserRole
import com.ilogistic.delivery_admin_backend.userpoint.domain.entity.QUserPoint.userPoint
import com.ilogistic.delivery_admin_backend.userpoint.enums.PointReason
import com.ilogistic.delivery_admin_backend.utils.QuerydslUtil
import com.querydsl.core.types.Projections
import com.querydsl.core.types.dsl.BooleanExpression
import com.querydsl.core.types.dsl.Expressions
import com.querydsl.jpa.impl.JPAQueryFactory
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.support.PageableExecutionUtils
import org.springframework.stereotype.Repository
import java.time.LocalDate
import java.time.temporal.TemporalAdjusters

@Repository
class ApplicationFeePaymentRepositoryImpl(
    private val queryFactory: JPAQueryFactory
) : ApplicationFeePaymentRepositoryCustom {

    override fun getPaymentCheck(userId: Long): Boolean {
        //이번달 1일
        val firstDay: String = LocalDate.now().with(TemporalAdjusters.firstDayOfMonth()).toString()
        //이번달 마지막날
        val lastDay: String = LocalDate.now().with(TemporalAdjusters.lastDayOfMonth()).toString()
//        println("firstDay : $firstDay")
//        println("lastDay : $lastDay")

        return queryFactory.select(applicationFeePayment.id)
            .from(applicationFeePayment)
            .where(
                applicationFeePayment.user.id.eq(userId)
                    .and(applicationFeePayment.status.eq(PaymentStatus.COMPLETE))
                    .and(QuerydslUtil.betweenDate(applicationFeePayment.createdDate, firstDay, lastDay))
            ).fetchFirst() != null
    }

    override fun getPaymentList(
        pageable: Pageable,
        applicationFeePaymentSearchDto: ApplicationFeePaymentSearchDto
    ): Page<ApplicationFeePaymentResponseDto> {
        val query = queryFactory.select(
            QApplicationFeePaymentResponseDto(
                applicationFeePayment.id,
                applicationFeePayment.user.id,
                applicationFeePayment.user.depositName,
                applicationFeePayment.user.userRole,
                applicationFeePayment.userPoint.deductedPoint,
                applicationFeePayment.userPoint.currentPoint,
                applicationFeePayment.createdDate
            )
        ).from(applicationFeePayment)
            .leftJoin(applicationFeePayment.user)
            .leftJoin(applicationFeePayment.userPoint)
            .where(search(applicationFeePaymentSearchDto.searchWord)
                    , roleSearch(applicationFeePaymentSearchDto.role)
                    , QuerydslUtil.betweenDate(applicationFeePayment.createdDate, applicationFeePaymentSearchDto.startDate, applicationFeePaymentSearchDto.endDate))
            .orderBy(applicationFeePayment.id.desc())
            .offset(pageable.offset)
            .limit(pageable.pageSize.toLong())
            .fetch()

        val count = queryFactory.select(applicationFeePayment.id.count())
            .from(applicationFeePayment)
            .leftJoin(applicationFeePayment.user)
            .where(search(applicationFeePaymentSearchDto.searchWord)
                , roleSearch(applicationFeePaymentSearchDto.role)
                , QuerydslUtil.betweenDate(applicationFeePayment.createdDate, applicationFeePaymentSearchDto.startDate, applicationFeePaymentSearchDto.endDate))
            .fetchFirst()

        return PageableExecutionUtils.getPage(query, pageable) {count}
    }

    override fun getPaymentTotalStatistics(applicationFeePaymentSearchDto: ApplicationFeePaymentSearchDto): ApplicationFeePaymentStatisticsResponseDto? {
        return queryFactory.select(
            QApplicationFeePaymentStatisticsResponseDto(
                applicationFeePayment.id.count(),
                applicationFeePayment.userPoint.deductedPoint.sum()
            )
        ).from(applicationFeePayment)
            .leftJoin(applicationFeePayment.user)
            .leftJoin(applicationFeePayment.userPoint)
            .where(search(applicationFeePaymentSearchDto.searchWord)
                    , roleSearch(applicationFeePaymentSearchDto.role)
                    , QuerydslUtil.betweenDate(applicationFeePayment.createdDate, applicationFeePaymentSearchDto.startDate, applicationFeePaymentSearchDto.endDate))
            .groupBy(applicationFeePayment.user.userRole)
            .fetchFirst()
    }

    override fun getPaymentTodayStatistics(pageable: Pageable, searchDto: ApplicationFeePaymentStatisticsDaySearchDto): Page<ApplicationFeePaymentStatisticsDayResponseDto> {
        val query = queryFactory.select(
            QApplicationFeePaymentStatisticsDayResponseDto(
//                CaseBuilder().`when`(applicationFeePayment.user.userRole.eq("ROLE_DRIVER")).then(applicationFeePayment.id.count()).otherwise(0).sum(),
//                CaseBuilder().`when`(applicationFeePayment.user.userRole.eq("ROLE_DRIVER")).then(applicationFeePayment.userPoint.deductedPoint).otherwise(0).sum(),
//                CaseBuilder().`when`(applicationFeePayment.user.userRole.eq("ROLE_FRANCHISEE")).then(applicationFeePayment.id.count()).otherwise(0).sum(),
//                CaseBuilder().`when`(applicationFeePayment.user.userRole.eq("ROLE_FRANCHISEE")).then(applicationFeePayment.userPoint.deductedPoint).otherwise(0).sum(),
                applicationFeePayment.id.count(),
                applicationFeePayment.userPoint.deductedPoint.sum(),
                Expressions.stringTemplate("DATE_FORMAT({0},{1})", applicationFeePayment.createdDate, "%Y%m%d"),
            )
        ).from(applicationFeePayment)
            .leftJoin(applicationFeePayment.user)
            .leftJoin(applicationFeePayment.userPoint)
            .where(
                roleSearch(searchDto.role)
                ,QuerydslUtil.betweenDate(applicationFeePayment.createdDate, searchDto.startDate, searchDto.endDate))
            .groupBy(Expressions.stringTemplate("DATE_FORMAT({0},{1})", applicationFeePayment.createdDate, "%Y%m%d"))
            .fetch()

        val count = queryFactory.select(applicationFeePayment.id.count())
            .from(applicationFeePayment)
            .leftJoin(applicationFeePayment.user)
            .leftJoin(applicationFeePayment.userPoint)
            .where(
                roleSearch(searchDto.role)
                ,QuerydslUtil.betweenDate(applicationFeePayment.createdDate, searchDto.startDate, searchDto.endDate))
            .groupBy(Expressions.stringTemplate("DATE_FORMAT({0},{1})", applicationFeePayment.createdDate, "%Y%m%d"))
            .fetchFirst()

        return PageableExecutionUtils.getPage(query, pageable) {count}

    }

//    val id: Long,
//    val reason: PointReason,
//    val amount: Int,
//    val balance: Int,
//    val createdDate: String
//
    override fun getPaymentListSelf(pageable: Pageable, userId: Long): Page<ApplicationPaymentSelfResponseDto> {
        val query = queryFactory.select(
            QApplicationPaymentSelfResponseDto(
                applicationFeePayment.id,
                userPoint.pointReason,
                userPoint.deductedPoint,
                userPoint.currentPoint,
                applicationFeePayment.createdDate
            )
        ).from(applicationFeePayment)
            .leftJoin(applicationFeePayment.userPoint, userPoint)
            .where(applicationFeePayment.user.id.eq(userId))
            .orderBy(applicationFeePayment.id.desc())
            .offset(pageable.offset)
            .limit(pageable.pageSize.toLong())
            .fetch()

        val count = queryFactory.select(applicationFeePayment.id.count())
            .from(applicationFeePayment)
            .leftJoin(applicationFeePayment.userPoint, userPoint)
            .where(applicationFeePayment.user.id.eq(userId))
            .fetchFirst()

        return PageableExecutionUtils.getPage(query, pageable) {count}
    }

    private fun search(searchWord: String?) : BooleanExpression? {
        if(!searchWord.isNullOrBlank()) {
            return applicationFeePayment.user.depositName.contains(searchWord)
        }

        return null
    }

    private fun roleSearch(userRole : UserRole?) : BooleanExpression? {
        if(userRole != null) {
            return applicationFeePayment.user.userRole.eq(userRole.value)
        }

        return null
    }

}
