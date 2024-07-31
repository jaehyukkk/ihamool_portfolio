package com.ilogistic.delivery_admin_backend.notice.repository

import com.ilogistic.delivery_admin_backend.exception.CustomMessageRuntimeException
import com.ilogistic.delivery_admin_backend.exception.CustomRuntimeException
import com.ilogistic.delivery_admin_backend.exception.ErrorCode
import com.ilogistic.delivery_admin_backend.notice.domain.dto.*
import com.ilogistic.delivery_admin_backend.notice.domain.entity.QNotice
import com.ilogistic.delivery_admin_backend.notice.domain.entity.QNotice.notice
import com.ilogistic.delivery_admin_backend.utils.QuerydslUtil
import com.querydsl.core.types.dsl.BooleanExpression
import com.querydsl.jpa.JPAExpressions
import com.querydsl.jpa.impl.JPAQueryFactory
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.support.PageableExecutionUtils
import org.springframework.stereotype.Repository
import org.springframework.util.StringUtils
import java.time.LocalDateTime

@Repository
class NoticeRepositoryImpl(
    private val queryFactory: JPAQueryFactory
) : NoticeRepositoryCustom {

    override fun getNoticePage(pageable: Pageable, noticeSearchDto: NoticeSearchDto): Page<NoticeListResponseDto> {
        val fetch = queryFactory.select(
            com.ilogistic.delivery_admin_backend.notice.domain.dto.QNoticeListResponseDto(
                notice.id, notice.title, notice.createdDate
            )
        ).from(notice)
            .where(QuerydslUtil.betweenDate(notice.createdDate, noticeSearchDto.startDate, noticeSearchDto.endDate), search(noticeSearchDto))
            .orderBy(notice.id.desc())
            .offset(pageable.offset)
            .limit(pageable.pageSize.toLong())
            .fetch()

        val count = queryFactory.select(notice.id.count())
            .from(notice)
            .where(QuerydslUtil.betweenDate(notice.createdDate, noticeSearchDto.startDate, noticeSearchDto.endDate), search(noticeSearchDto))
            .fetchFirst()
        return PageableExecutionUtils.getPage(fetch, pageable) {count}

    }

    private fun search(noticeSearchDto: NoticeSearchDto) : BooleanExpression? {
        if (StringUtils.hasText(noticeSearchDto.searchWord)) {
            return notice.title.contains(noticeSearchDto.searchWord)
        }
        return null
    }

    override fun getNoticeDetail(id: Long): NoticeDetailResponseDto {
        val notice = queryFactory.select(
            com.ilogistic.delivery_admin_backend.notice.domain.dto.QNoticeDetailResponseDto(
                notice.id,
                notice.title,
                notice.content,
                notice.createdDate,
                notice.updatedDate,
                notice.hit
            )
        ).from(notice)
            .leftJoin(notice.user)
            .where(notice.id.eq(id))
            .fetchOne()

        notice ?: throw CustomRuntimeException(ErrorCode.ENTITY_NOT_FOUND)

        val prev = queryFactory.select(
            com.ilogistic.delivery_admin_backend.notice.domain.dto.QSubPostDto(
                QNotice.notice.id,
                QNotice.notice.title
            )
        ).from(QNotice.notice)
            .where(
                QNotice.notice.id.eq(
                JPAExpressions.select(QNotice.notice.id.max())
                .from(QNotice.notice)
                .where(QNotice.notice.id.lt(id))))
            .fetchOne()

        val next = queryFactory.select(
            com.ilogistic.delivery_admin_backend.notice.domain.dto.QSubPostDto(
                QNotice.notice.id,
                QNotice.notice.title
            )
        ).from(QNotice.notice)
            .where(
                QNotice.notice.id.eq(JPAExpressions.select(QNotice.notice.id.min())
                .from(QNotice.notice)
                .where(QNotice.notice.id.gt(id))))
            .fetchOne()

        notice.prevId = prev?.id
        notice.prevTitle = prev?.title
        notice.nextId = next?.id
        notice.nextTitle = next?.title

        return notice
    }
}
