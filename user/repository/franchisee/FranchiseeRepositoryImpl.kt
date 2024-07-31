package com.ilogistic.delivery_admin_backend.user.repository.franchisee

import com.ilogistic.delivery_admin_backend.exception.BaseException
import com.ilogistic.delivery_admin_backend.exception.CustomRuntimeException
import com.ilogistic.delivery_admin_backend.exception.ErrorCode
import com.ilogistic.delivery_admin_backend.user.domain.dto.*
import com.ilogistic.delivery_admin_backend.user.domain.entity.QDriver
import com.ilogistic.delivery_admin_backend.user.domain.entity.QFranchisee.franchisee
import com.querydsl.core.types.dsl.BooleanExpression
import com.querydsl.jpa.impl.JPAQueryFactory
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.support.PageableExecutionUtils
import org.springframework.stereotype.Repository

@Repository
class FranchiseeRepositoryImpl(
    private val queryFactory: JPAQueryFactory
) : FranchiseeRepositoryCustom{


    override fun getFranchiseeList(
        franchiseeSearchDto: FranchiseeSearchDto,
        pageable: Pageable
    ): Page<FranchiseSearchResponseDto> {
        val query = queryFactory.select(
            QFranchiseSearchResponseDto(
                franchisee.id,
                franchisee.companyName,
                franchisee.managerPhone,
                franchisee.address,
                franchisee.detailAddress,
            )
        ).from(franchisee)
            .where(search(franchiseeSearchDto))
            .orderBy(franchisee.id.desc())
            .offset(pageable.offset)
            .limit(pageable.pageSize.toLong())

        val count = queryFactory.select(franchisee.id.count())
            .from(franchisee)
            .where(search(franchiseeSearchDto))
            .fetchFirst()

        return PageableExecutionUtils.getPage(query.fetch(), pageable) {count}
    }

    override fun getFranchiseeInfo(franchiseeId: Long): FranchiseeInfoResponseDto {
        return queryFactory.select(
            QFranchiseeInfoResponseDto(
                franchisee.id,
                franchisee.companyName,
                franchisee.companyNumber,
                franchisee.managerName,
                franchisee.managerPhone,
                franchisee.address,
                franchisee.detailAddress,
                franchisee.zipCode,
                franchisee.bankNumber,
                franchisee.bank,
                franchisee.bankOwner,
            )
        ).from(franchisee)
            .where(franchisee.id.eq(franchiseeId))
            .fetchOne() ?: throw BaseException(ErrorCode.ENTITY_NOT_FOUND)
    }

    private fun search(franchiseeSearchDto: FranchiseeSearchDto) : BooleanExpression? {
        if (franchiseeSearchDto.search.isNullOrBlank() || franchiseeSearchDto.searchType.isNullOrBlank()) {
            throw CustomRuntimeException(ErrorCode.BAD_REQUEST)
        }
        return when(franchiseeSearchDto.searchType) {
            "1" -> franchisee.companyName.contains(franchiseeSearchDto.search)
            "2" -> franchisee.managerPhone.contains(franchiseeSearchDto.search)
            else -> throw CustomRuntimeException(ErrorCode.BAD_REQUEST)
        }
    }
}
