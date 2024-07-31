package com.ilogistic.delivery_admin_backend.cartype.domain.repository

import com.ilogistic.delivery_admin_backend.cartype.domain.dto.CarTypeResponseDto
import com.ilogistic.delivery_admin_backend.cartype.domain.dto.CarTypeSearchDto
import com.ilogistic.delivery_admin_backend.cartype.domain.dto.ParentListResponseDto
import com.ilogistic.delivery_admin_backend.cartype.domain.dto.QCarTypeResponseDto
import com.ilogistic.delivery_admin_backend.cartype.domain.entity.QCarType
import com.ilogistic.delivery_admin_backend.cartype.domain.entity.QCarType.carType
import com.ilogistic.delivery_admin_backend.exception.CustomRuntimeException
import com.ilogistic.delivery_admin_backend.exception.ErrorCode
import com.querydsl.core.types.ExpressionUtils
import com.querydsl.core.types.Projections
import com.querydsl.core.types.dsl.BooleanExpression
import com.querydsl.core.types.dsl.CaseBuilder
import com.querydsl.jpa.JPAExpressions
import com.querydsl.jpa.impl.JPAQueryFactory
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.support.PageableExecutionUtils
import org.springframework.stereotype.Repository

@Repository
class CarTypeRepositoryImpl(
    private val queryFactory: JPAQueryFactory,
) :  CarTypeRepositoryCustom{


    private fun defaultSelect() : QCarTypeResponseDto {
        val carTypeSub = QCarType("carTypeSub")

        return QCarTypeResponseDto(
            carType.id,
            carType.name,
            carType.description,
            carType.parentId,
            CaseBuilder()
                .`when`(carType.parentId.isNull)
                .then("")
                .otherwise(
                    JPAExpressions.select(
                        carTypeSub.name
                    ).from(carTypeSub)
                        .where(carTypeSub.id.eq(carType.parentId))
                ),
            carType.ton,
            carType.createdDate
        )
    }
    override fun getCarTypeList(pageable: Pageable, carTypeSearchDto: CarTypeSearchDto): Page<CarTypeResponseDto> {
        val query = queryFactory.select(defaultSelect())
            .from(carType)
            .where(
                rank(carTypeSearchDto)
            )
            .orderBy(carType.id.desc())
            .offset(pageable.offset)
            .limit(pageable.pageSize.toLong())

        val count = queryFactory.select(carType.id.count())
            .from(carType)
            .where(
                rank(carTypeSearchDto)
            )
            .fetchFirst()

        return PageableExecutionUtils.getPage(query.fetch(), pageable) {count}
    }

    override fun getParentList(): List<ParentListResponseDto> {
        return queryFactory.select(
            Projections.constructor(
                ParentListResponseDto::class.java,
                carType.id,
                carType.ton,
                carType.name
            )
        )
            .from(carType)
            .where(carType.parentId.isNull)
            .fetch()
    }

    override fun getChildrenList(id: Long): List<CarTypeResponseDto> {
        return queryFactory.select(defaultSelect())
            .from(carType)
            .where(carType.parentId.eq(id))
            .fetch()
    }

    private fun rank(carTypeSearchDto: CarTypeSearchDto) : BooleanExpression? {
        if (carTypeSearchDto.rank.isNullOrBlank()) {
            return null
        }
        return when(carTypeSearchDto.rank) {
            "1" -> carType.parentId.isNull
            "2" -> carType.parentId.isNotNull
            else -> throw CustomRuntimeException(ErrorCode.BAD_REQUEST)
        }
    }

}
