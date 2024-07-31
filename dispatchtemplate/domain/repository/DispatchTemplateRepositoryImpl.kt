package com.ilogistic.delivery_admin_backend.dispatchtemplate.domain.repository

import com.ilogistic.delivery_admin_backend.admingroup.domain.entity.QAdminGroup.adminGroup
import com.ilogistic.delivery_admin_backend.cartype.domain.entity.QCarType
import com.ilogistic.delivery_admin_backend.cartype.domain.entity.QCarType.carType
import com.ilogistic.delivery_admin_backend.dispatch.domain.entity.QDispatch
import com.ilogistic.delivery_admin_backend.dispatch.domain.entity.QDispatch.dispatch
import com.ilogistic.delivery_admin_backend.dispatch.enums.DispatchDateValue
import com.ilogistic.delivery_admin_backend.dispatch.enums.DispatchStatus
import com.ilogistic.delivery_admin_backend.dispatch.enums.LoadingMethod
import com.ilogistic.delivery_admin_backend.dispatch.enums.PaymentType
import com.ilogistic.delivery_admin_backend.dispatchcartype.domain.entity.QDispatchCarType.dispatchCarType
import com.ilogistic.delivery_admin_backend.dispatchtemplate.domain.dto.DispatchTemplateResponseDto
import com.ilogistic.delivery_admin_backend.dispatchtemplate.domain.dto.DispatchTemplateSearchDto
import com.ilogistic.delivery_admin_backend.dispatchtemplate.domain.dto.QDispatchTemplateResponseDto
import com.ilogistic.delivery_admin_backend.dispatchtemplate.domain.entity.QDispatchTemplate.dispatchTemplate
import com.ilogistic.delivery_admin_backend.dispatchtemplatecartype.domain.entity.QDispatchTemplateCarType.dispatchTemplateCarType
import com.ilogistic.delivery_admin_backend.exception.BaseException
import com.ilogistic.delivery_admin_backend.exception.ErrorCode
import com.ilogistic.delivery_admin_backend.user.domain.entity.QUser
import com.ilogistic.delivery_admin_backend.user.domain.entity.QUser.user
import com.ilogistic.delivery_admin_backend.utils.QuerydslUtil
import com.querydsl.core.types.dsl.BooleanExpression
import com.querydsl.core.types.dsl.Expressions
import com.querydsl.jpa.JPAExpressions
import com.querydsl.jpa.impl.JPAQueryFactory
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.support.PageableExecutionUtils
import org.springframework.stereotype.Repository

@Repository
class DispatchTemplateRepositoryImpl(
    private val queryFactory: JPAQueryFactory
) : DispatchTemplateRepositoryCustom {

    private fun defaultSelect(): QDispatchTemplateResponseDto {
        val carType1 = QCarType("carType1")
        return QDispatchTemplateResponseDto(
            dispatchTemplate.id,
            dispatchTemplate.templateName,
            dispatchTemplate.startAddress,
            dispatchTemplate.startDetailAddress,
            dispatchTemplate.startZipCode,
            dispatchTemplate.startSigungu,
            dispatchTemplate.startSido,
            dispatchTemplate.endAddress,
            dispatchTemplate.endDetailAddress,
            dispatchTemplate.endZipCode,
            dispatchTemplate.endSigungu,
            dispatchTemplate.endSido,
            dispatchTemplate.originalPrice,
            dispatchTemplate.fee,
            dispatchTemplate.driverPrice,
            dispatchTemplate.loadingMethod,
            dispatchTemplate.itemName,
            dispatchTemplate.itemCount,
            dispatchTemplate.palletCount,
            dispatchTemplate.precautions,
            dispatchTemplate.memo,
            dispatchTemplate.dispatchStart,
            dispatchTemplate.dispatchEnd,
            dispatchTemplate.paymentType,
            dispatchTemplate.franchisee.id,
            dispatchTemplate.franchisee.companyName,
            dispatchTemplate.franchisee.managerPhone,
            dispatchTemplate.franchisee.address,
            dispatchTemplate.franchisee.detailAddress,
            dispatchTemplate.shipPrice,
            dispatchTemplate.addressType,
//            dispatchTemplate.carTypeIds
            JPAExpressions.select(
                Expressions.stringTemplate(
                    "GROUP_CONCAT({0})",
                    dispatchTemplateCarType.carType.id
                )
            ).from(dispatchTemplateCarType)
                .where(dispatchTemplateCarType.dispatchTemplate.id.eq(dispatchTemplate.id))
                .leftJoin(dispatchTemplateCarType.carType),
            JPAExpressions.select(
                Expressions.stringTemplate(
                    "GROUP_CONCAT({0})",
                    dispatchTemplateCarType.carType.name
                )
            ).from(dispatchTemplateCarType)
                .where(dispatchTemplateCarType.dispatchTemplate.id.eq(dispatchTemplate.id))
                .leftJoin(dispatchTemplateCarType.carType),
            JPAExpressions.select(
//                Expressions.stringTemplate(
//                    "CONCAT({0}, {3}, {1}, {3}, {2})",
//                    carType.name,
//                    carType.id,
//                    carType.ton,
//                    Expressions.constant(","),
//                )
                carType.name.concat(",")
                    .concat(carType.id.stringValue())
                    .concat(",")
                    .concat(carType.ton.stringValue())
            )
                .from(carType)
                .where(
                    carType.id.eq(
                        JPAExpressions.select(carType1.parentId)
                            .from(carType1)
                            .where(carType1.id.eq(
                                JPAExpressions.select(dispatchTemplateCarType.carType.id.max())
                                    .from(dispatchTemplateCarType)
                                    .where(dispatchTemplateCarType.dispatchTemplate.id.eq(dispatchTemplate.id))
                            ))
                    )),
        )
    }
    override fun getDispatchTemplateList(pageable: Pageable, userId: Long, dispatchTemplateSearchDto: DispatchTemplateSearchDto): Page<DispatchTemplateResponseDto> {
        val user1 = QUser("user")
        val query = queryFactory.select(
            defaultSelect()
        ).from(dispatchTemplate)
            .leftJoin(dispatchTemplate.franchisee)
            .leftJoin(dispatchTemplate.user, user)
            .leftJoin(user.adminGroup, adminGroup)
            .where(adminGroup.id.eq(
                JPAExpressions.select(user1.adminGroup.id)
                    .from(user1)
                    .leftJoin(user1.adminGroup)
                    .where(user1.id.eq(userId))
            ), search(dispatchTemplateSearchDto))
            .orderBy(dispatchTemplate.id.desc())
            .offset(pageable.offset)
            .limit(pageable.pageSize.toLong())
            .fetch()

    val count  = queryFactory.select(dispatchTemplate.id.count())
        .from(dispatchTemplate)
        .leftJoin(dispatchTemplate.franchisee)
        .leftJoin(dispatchTemplate.user)
        .where(dispatchTemplate.user.id.eq(userId), search(dispatchTemplateSearchDto))

        .fetchFirst()

    return PageableExecutionUtils.getPage(query, pageable) {count}
    }

    override fun getDispatchTemplate(id: Long): DispatchTemplateResponseDto {
        return queryFactory.select(
            defaultSelect()
        ).from(dispatchTemplate)
            .leftJoin(dispatchTemplate.franchisee)
            .where(dispatchTemplate.id.eq(id))
            .fetchOne() ?: throw BaseException(ErrorCode.ENTITY_NOT_FOUND)

    }

    private fun search(dispatchTemplateSearchDto: DispatchTemplateSearchDto): BooleanExpression? {
        if(!dispatchTemplateSearchDto.search.isNullOrBlank()){
            return when(dispatchTemplateSearchDto.searchType){
                "1" -> dispatchTemplate.templateName.contains(dispatchTemplateSearchDto.search)
                "2" -> dispatchTemplate.startAddress.contains(dispatchTemplateSearchDto.search)
                "3" -> dispatchTemplate.endAddress.contains(dispatchTemplateSearchDto.search)
                "4" -> dispatchTemplate.itemName.contains(dispatchTemplateSearchDto.search)
                "5" -> dispatchTemplate.franchisee.companyName.contains(dispatchTemplateSearchDto.search)

                else -> null
            }
        }
        return null
    }
}
