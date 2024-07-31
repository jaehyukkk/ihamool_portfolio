package com.ilogistic.delivery_admin_backend.chargerequest.domain.repository

import com.ilogistic.delivery_admin_backend.chargerequest.domain.dto.ChargeRequestAllUserListResponseDto
import com.ilogistic.delivery_admin_backend.chargerequest.domain.dto.ChargeRequestApproveRequestDto
import com.ilogistic.delivery_admin_backend.chargerequest.domain.dto.ChargeRequestListResponseDto
import com.ilogistic.delivery_admin_backend.chargerequest.domain.dto.ChargeRequestSearchDto
import com.ilogistic.delivery_admin_backend.chargerequest.domain.entity.ChargeRequest
import com.ilogistic.delivery_admin_backend.chargerequest.domain.entity.QChargeRequest.chargeRequest
import com.ilogistic.delivery_admin_backend.chargerequest.enums.ChargeRequestStatus
import com.ilogistic.delivery_admin_backend.user.domain.entity.QUser.user
import com.ilogistic.delivery_admin_backend.user.enums.UserRole
import com.ilogistic.delivery_admin_backend.utils.QuerydslUtil
import com.querydsl.core.types.Projections
import com.querydsl.core.types.dsl.BooleanExpression
import com.querydsl.jpa.impl.JPAQueryFactory
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.support.PageableExecutionUtils
import org.springframework.stereotype.Repository
import java.time.LocalDateTime

@Repository
class ChargeRequestRepositoryImpl(
    private val queryFactory: JPAQueryFactory
) : ChargeRequestRepositoryCustom{

    override fun getApproveTargetChargeRequest(chargeRequestApproveRequestDto: ChargeRequestApproveRequestDto): ChargeRequest? {
        return queryFactory.select(chargeRequest)
            .from(chargeRequest)
            .leftJoin(chargeRequest.user)
            .orderBy(chargeRequest.createdDate.desc())
            .where(
                chargeRequest.point.eq(chargeRequestApproveRequestDto.point)
                    .and(chargeRequest.user.depositName.eq(chargeRequestApproveRequestDto.name))
                    .and(
                        chargeRequest.createdDate.gt(LocalDateTime.now().minusHours(1))
                    )
            ).fetchFirst()

    }

    override fun getChargeRequestList(pageable: Pageable, userId: Long): Page<ChargeRequestListResponseDto> {
        val query = queryFactory.select(
            Projections.constructor(
                ChargeRequestListResponseDto::class.java,
                chargeRequest.id,
                chargeRequest.createdDate,
                chargeRequest.point,
                chargeRequest.status,

            )
        ).from(chargeRequest)
            .where(chargeRequest.user.id.eq(userId))
            .orderBy(chargeRequest.createdDate.desc())
            .offset(pageable.offset)
            .limit(pageable.pageSize.toLong())
            .fetch()

        val count = queryFactory.select(chargeRequest.id.count())
            .from(chargeRequest)
            .where(chargeRequest.user.id.eq(userId))
            .fetchFirst()

        return PageableExecutionUtils.getPage(query, pageable) {count}

    }

    override fun getAllChargeRequestList(
        pageable: Pageable,
        chargeRequestSearchDto: ChargeRequestSearchDto
    ): Page<ChargeRequestAllUserListResponseDto> {
        val query = queryFactory.select(
            Projections.constructor(
                ChargeRequestAllUserListResponseDto::class.java,
                chargeRequest.id,
                chargeRequest.createdDate,
                chargeRequest.point,
                chargeRequest.status,
                chargeRequest.user.id,
                chargeRequest.user.depositName,
                chargeRequest.user.username,
                chargeRequest.user.userRole,
            )
        ).from(chargeRequest)
            .leftJoin(chargeRequest.user)
            .where(
                searchRole(chargeRequestSearchDto.role),
                search(chargeRequestSearchDto),
                status(chargeRequestSearchDto),
                QuerydslUtil.betweenDate(chargeRequest.createdDate, chargeRequestSearchDto.startDate, chargeRequestSearchDto.endDate)
            )
            .orderBy(chargeRequest.createdDate.desc())
            .offset(pageable.offset)
            .limit(pageable.pageSize.toLong())
            .fetch()

        val count = queryFactory.select(chargeRequest.id.count())
            .from(chargeRequest)
            .leftJoin(chargeRequest.user)
            .where(
                searchRole(chargeRequestSearchDto.role),
                search(chargeRequestSearchDto),
                status(chargeRequestSearchDto),
                QuerydslUtil.betweenDate(chargeRequest.createdDate, chargeRequestSearchDto.startDate, chargeRequestSearchDto.endDate)
            )
            .fetchFirst()

        return PageableExecutionUtils.getPage(query, pageable) {count}
    }

    override fun sum(chargeRequestSearchDto: ChargeRequestSearchDto): Int {
        return queryFactory.select(chargeRequest.point.sum())
            .from(chargeRequest)
            .where(
                search(chargeRequestSearchDto),
                chargeRequest.status.eq(ChargeRequestStatus.ACCEPTED),
                QuerydslUtil.betweenDate(chargeRequest.createdDate, chargeRequestSearchDto.startDate, chargeRequestSearchDto.endDate)
            )
            .fetchOne() ?: 0
    }

    private fun search(chargeRequestSearchDto: ChargeRequestSearchDto): BooleanExpression? {
        if(!chargeRequestSearchDto.searchWord.isNullOrBlank()) {
            if(chargeRequestSearchDto.searchType == "1") {
                return chargeRequest.user.depositName.contains(chargeRequestSearchDto.searchWord)
            }
            if(chargeRequestSearchDto.searchType == "2") {
                return chargeRequest.user.username.contains(chargeRequestSearchDto.searchWord)
            }
        }

        return null
    }
    private fun searchRole(role: UserRole?): BooleanExpression? {
        if(role == null) {
            return null
        }
        return chargeRequest.user.userRole.eq(role.value)
    }

    private fun status(chargeRequestSearchDto: ChargeRequestSearchDto): BooleanExpression? {
        if(chargeRequestSearchDto.status != null) {
            return chargeRequest.status.eq(chargeRequestSearchDto.status)
        }

        return null
    }
}
