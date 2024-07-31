package com.ilogistic.delivery_admin_backend.dispatch.domain.repository

import com.ilogistic.delivery_admin_backend.admingroup.domain.entity.QAdminGroup.adminGroup
import com.ilogistic.delivery_admin_backend.cartype.domain.dto.CarTypeBriefResponseDto
import com.ilogistic.delivery_admin_backend.cartype.domain.dto.CarTypeResponseDto
import com.ilogistic.delivery_admin_backend.cartype.domain.entity.QCarType
import com.ilogistic.delivery_admin_backend.cartype.domain.entity.QCarType.carType
import com.ilogistic.delivery_admin_backend.dispatch.domain.dto.*
import com.ilogistic.delivery_admin_backend.dispatch.domain.dto.DispatchSocketSendInfoDto.Companion.CarTypeIdResponse
import com.ilogistic.delivery_admin_backend.dispatch.domain.entity.QDispatch.dispatch
import com.ilogistic.delivery_admin_backend.dispatch.enums.DispatchSearchCase
import com.ilogistic.delivery_admin_backend.dispatch.enums.DispatchSearchType
import com.ilogistic.delivery_admin_backend.dispatch.enums.DispatchStatus
import com.ilogistic.delivery_admin_backend.dispatch.enums.PaymentType
import com.ilogistic.delivery_admin_backend.dispatchcartype.domain.entity.QDispatchCarType.dispatchCarType
import com.ilogistic.delivery_admin_backend.dispatchcomplete.domain.entity.QDispatchComplete.dispatchComplete
import com.ilogistic.delivery_admin_backend.exception.BaseException
import com.ilogistic.delivery_admin_backend.exception.ErrorCode
import com.ilogistic.delivery_admin_backend.user.domain.entity.QDriver.driver
import com.ilogistic.delivery_admin_backend.user.domain.entity.QUser
import com.ilogistic.delivery_admin_backend.user.domain.entity.QUser.user
import com.ilogistic.delivery_admin_backend.utils.QuerydslUtil
import com.querydsl.core.group.GroupBy.groupBy
import com.querydsl.core.group.GroupBy.set
import com.querydsl.core.types.Projections
import com.querydsl.core.types.dsl.BooleanExpression
import com.querydsl.core.types.dsl.CaseBuilder
import com.querydsl.core.types.dsl.Expressions
import com.querydsl.jpa.JPAExpressions
import com.querydsl.jpa.impl.JPAQueryFactory
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.support.PageableExecutionUtils
import org.springframework.stereotype.Repository

@Repository
class DispatchRepositoryImpl(
    private val queryFactory: JPAQueryFactory
) : DispatchRepositoryCustom {

    private fun defaultSelect(): QDispatchResponseDto {
        return QDispatchResponseDto(
            dispatch.id!!,
            dispatch.startAddress,
            dispatch.startDetailAddress,
            dispatch.startZipCode,
            dispatch.startSigungu,
            dispatch.startSido,
            dispatch.startLatitude,
            dispatch.startLongitude,
            dispatch.endAddress,
            dispatch.endDetailAddress,
            dispatch.endZipCode,
            dispatch.endSigungu,
            dispatch.endSido,
            dispatch.endLatitude,
            dispatch.endLongitude,
            dispatch.viaAddress,
            dispatch.viaDetailAddress,
            dispatch.viaZipCode,
            dispatch.viaSigungu,
            dispatch.viaSido,
            dispatch.viaLatitude,
            dispatch.viaLongitude,
            dispatch.distance,
            dispatch.originalPrice,
            dispatch.fee,
            dispatch.driverPrice,
            dispatch.loadingMethod,
            dispatch.itemName,
            dispatch.itemCount,
            dispatch.palletCount,
            dispatch.parentCarType.ton,
            dispatch.precautions,
            dispatch.memo,
            dispatch.dispatchStart,
            dispatch.dispatchEnd,
            dispatch.status,
            dispatch.paymentType,
            dispatch.franchisee.id!!,
            dispatch.franchisee.companyName,
            dispatch.franchisee.managerPhone,
            dispatch.franchisee.address,
            dispatch.franchisee.detailAddress,
            dispatch.shipPrice,
            dispatch.dispatchDateTime,
            dispatch.completeDateTime,
            dispatch.addressType,
            dispatch.dispatchCode
        )
    }

    private fun defaultSelectV2(): QDispatchResponseDtoV2 {
        return QDispatchResponseDtoV2(
            dispatch.id!!,
            dispatch.startAddress,
            dispatch.startDetailAddress,
            dispatch.startZipCode,
            dispatch.startSigungu,
            dispatch.startSido,
            dispatch.startLatitude,
            dispatch.startLongitude,
            dispatch.endAddress,
            dispatch.endDetailAddress,
            dispatch.endZipCode,
            dispatch.endSigungu,
            dispatch.endSido,
            dispatch.endLatitude,
            dispatch.endLongitude,
            dispatch.viaAddress,
            dispatch.viaDetailAddress,
            dispatch.viaZipCode,
            dispatch.viaSigungu,
            dispatch.viaSido,
            dispatch.viaLatitude,
            dispatch.viaLongitude,
            dispatch.distance,
            dispatch.originalPrice,
            dispatch.fee,
            dispatch.driverPrice,
            dispatch.loadingMethod,
            dispatch.itemName,
            dispatch.itemCount,
            dispatch.palletCount,
            dispatch.parentCarType.ton,
            dispatch.precautions,
            dispatch.memo,
            dispatch.dispatchStart,
            dispatch.dispatchEnd,
            dispatch.status,
            dispatch.paymentType,
            dispatch.franchisee.id!!,
            dispatch.franchisee.companyName,
            dispatch.franchisee.managerPhone,
            dispatch.franchisee.address,
            dispatch.franchisee.detailAddress,
            dispatch.driver.id,
            dispatch.driver.name,
            dispatch.driver.phone,
            dispatch.driver.carNumber,

            dispatch.shipPrice,
            dispatch.dispatchDateTime,
            dispatch.completeDateTime,
            dispatch.addressType,
            dispatch.dispatchCode,

            dispatch.createdDate
        )
    }

    override fun getDispatchList(): List<DispatchResponseDto>? {
        return queryFactory.select(
            defaultSelect()
        ).from(dispatch)
            .leftJoin(dispatch.franchisee)
            .leftJoin(dispatch.parentCarType)
            .orderBy(dispatch.id.asc())
            .where(dispatch.driver.id.isNull)
            .fetch()
    }

    override fun getDispatchWaitingListMatchingCarType(driverId: Long): List<DispatchResponseDto>? {
        return queryFactory.select(defaultSelect())
            .from(dispatch)
            .leftJoin(dispatch.franchisee)
            .leftJoin(dispatch.parentCarType)
            .innerJoin(dispatchCarType).on(dispatch.id.eq(dispatchCarType.dispatch.id))
            .innerJoin(driver).on(dispatchCarType.carType.id.eq(driver.carType.id))
            .where(driver.id.eq(driverId), dispatch.status.eq(DispatchStatus.WAITING))
            .fetch()
    }

    override fun getDispatchPageList(
        pageable: Pageable,
        dispatchSearchDto: DispatchSearchDto,
    ): Page<DispatchPageListResponseDto> {
        val query = queryFactory.select(
            QDispatchPageListResponseDto(
                dispatch.id!!,
                dispatch.startAddress,
                dispatch.endAddress,
                dispatch.itemName,
                dispatch.paymentType,
                dispatch.originalPrice,
                dispatch.fee,
                dispatch.driverPrice,
                dispatch.loadingMethod,
                dispatch.driver.id,
                dispatch.driver.name,
                dispatch.driver.phone,
                dispatch.driver.carNumber,
                dispatch.shipPrice,
                dispatch.status,
                dispatch.distance,
                dispatch.parentCarType.ton,
                dispatch.createdDate
            )
        ).from(dispatch)
            .leftJoin(dispatch.driver)
            .leftJoin(dispatch.user, user)
            .leftJoin(dispatch.parentCarType)
            .where(
                status(dispatchSearchDto.status),
                statusList(dispatchSearchDto.statusList)
                , QuerydslUtil.betweenDate(dispatch.createdDate, dispatchSearchDto.startDate, dispatchSearchDto.endDate)
                , paymentType(dispatchSearchDto.paymentType)
                , searchCase(dispatchSearchDto)
                , search(dispatchSearchDto)
            )
            .limit(pageable.pageSize.toLong())
            .offset(pageable.offset.toLong())
            .orderBy(dispatch.id.desc())
            .fetch()

        val count  = queryFactory.select(dispatch.id.count())
            .from(dispatch)
            .leftJoin(dispatch.user, user)
            .where(
                status(dispatchSearchDto.status),
                statusList(dispatchSearchDto.statusList)
                , QuerydslUtil.betweenDate(dispatch.createdDate, dispatchSearchDto.startDate, dispatchSearchDto.endDate)
                , paymentType(dispatchSearchDto.paymentType)
                , searchCase(dispatchSearchDto)
                , search(dispatchSearchDto)
            )
            .fetchFirst()

        return PageableExecutionUtils.getPage(query, pageable) {count}

    }

    override fun getDispatchPageListStatistics(dispatchSearchDto: DispatchSearchDto): DispatchStatisticsResponseDto? {
        return queryFactory.select(
            Projections.constructor(
                DispatchStatisticsResponseDto::class.java,
                dispatch.id.count(),
                CaseBuilder().`when`(dispatch.status.eq(DispatchStatus.COMPLETED)).then(dispatch.originalPrice).otherwise(0).sum(),
                CaseBuilder().`when`(dispatch.status.ne(DispatchStatus.COMPLETED)).then(dispatch.originalPrice).otherwise(0).sum(),
            )
        ).from(dispatch)
            .where(
                status(dispatchSearchDto.status),
                statusList(dispatchSearchDto.statusList),
                QuerydslUtil.betweenDate(dispatch.createdDate, dispatchSearchDto.startDate, dispatchSearchDto.endDate),
                paymentType(dispatchSearchDto.paymentType),
                searchFranchisee(dispatchSearchDto.searchUserId),
                search(dispatchSearchDto)

            )
            .fetchOne()
    }
    override fun getDispatchModifyInfo(dispatchId: Long): DispatchModifyInfoDto? {
        return queryFactory.select(
            QDispatchModifyInfoDto(
                dispatch.status, dispatch.dispatchCode
            )
        )
            .from(dispatch)
            .where(dispatch.id.eq(dispatchId))
            .fetchOne()
    }

    override fun getDispatchStatus(dispatchId: Long): DispatchStatus? {
        return queryFactory.select(dispatch.status)
            .from(dispatch)
            .where(dispatch.id.eq(dispatchId))
            .fetchOne()
    }
    override fun getDispatchDetail(id: Long): DispatchResponseDto? {
        return queryFactory.select(defaultSelect())
            .from(dispatch)
            .leftJoin(dispatch.franchisee)
            .where(dispatch.id.eq(id))
            .fetchOne()
    }


    override fun getDispatchDetailV2(id: Long): DispatchResponseDtoV2? {
        return queryFactory.select(defaultSelectV2())
            .from(dispatch)
            .leftJoin(dispatch.franchisee)
            .leftJoin(dispatch.driver)
            .where(dispatch.id.eq(id))
            .fetchOne()

    }

    override fun getDispatchModifyDetail(id : Long): DispatchModifyResponseDto? {
        return queryFactory.selectFrom(dispatch)
            .from(dispatch)
            .leftJoin(dispatch.franchisee)
            .leftJoin(dispatch.parentCarType)
            .leftJoin(dispatch.dispatchCarTypes, dispatchCarType)
            .leftJoin(dispatchCarType.carType)
            .where(dispatch.id.eq(id))
            .transform(groupBy(dispatch.id).list(
                Projections.constructor(
                    DispatchModifyResponseDto::class.java,
                    dispatch.id!!,
                    dispatch.startAddress,
                    dispatch.startDetailAddress,
                    dispatch.startZipCode,
                    dispatch.startSigungu,
                    dispatch.startSido,
                    dispatch.startLatitude,
                    dispatch.startLongitude,
                    dispatch.endAddress,
                    dispatch.endDetailAddress,
                    dispatch.endZipCode,
                    dispatch.endSigungu,
                    dispatch.endSido,
                    dispatch.endLatitude,
                    dispatch.endLongitude,
                    dispatch.viaAddress,
                    dispatch.viaDetailAddress,
                    dispatch.viaZipCode,
                    dispatch.viaSigungu,
                    dispatch.viaSido,
                    dispatch.viaLatitude,
                    dispatch.viaLongitude,
                    dispatch.distance,
                    dispatch.originalPrice,
                    dispatch.fee,
                    dispatch.driverPrice,
                    dispatch.loadingMethod,
                    dispatch.itemName,
                    dispatch.itemCount,
                    dispatch.palletCount,
                    dispatch.precautions,
                    dispatch.memo,
                    dispatch.dispatchStart,
                    dispatch.dispatchEnd,
                    dispatch.status,
                    dispatch.paymentType,
                    dispatch.franchisee.id!!,
                    dispatch.franchisee.companyName,
                    dispatch.franchisee.managerPhone,
                    dispatch.franchisee.address,
                    dispatch.franchisee.detailAddress,
                    dispatch.shipPrice,
                    dispatch.addressType,
                    dispatch.parentCarType.name,
                    set(
                        Projections.constructor(
                            DispatchModifyResponseDto.Companion.CarTypeResponse::class.java,
                            dispatchCarType.carType.id,
                            dispatchCarType.carType.name
                        )
                    ),
                    dispatch.parentCarType.id,
                    dispatch.parentCarType.ton
                )))
            .first()
    }

    override fun getDispatchSocketSendInfoDto(id: Long): DispatchSocketSendInfoDto {
        return queryFactory.selectFrom(dispatch)
            .from(dispatch)
            .leftJoin(dispatch.franchisee)
            .leftJoin(dispatch.dispatchCarTypes, dispatchCarType)
            .leftJoin(dispatchCarType.carType)
            .leftJoin(dispatch.adminGroup, adminGroup)
            .leftJoin(dispatch.driver)
            .where(dispatch.id.eq(id))
            .transform(groupBy(dispatch.id).list(
                Projections.constructor(
                    DispatchSocketSendInfoDto::class.java,
                    dispatch.id,
                    dispatch.dispatchCode,
                    dispatch.franchisee.id,
                    adminGroup.groupCode,
                    dispatch.status,
                    dispatch.driver.name,
                    set(
                        Projections.constructor(
                            CarTypeIdResponse::class.java,
                            dispatchCarType.carType.id,
                        )
                    ),
                )))
            .first() ?: throw BaseException(ErrorCode.ENTITY_NOT_FOUND)
    }

    override fun getCompanyWaitDispatchList(id: Long): List<DispatchResponseDto>? {
        return queryFactory.select(defaultSelect())
            .from(dispatch)
            .leftJoin(dispatch.franchisee)
            .leftJoin(dispatch.parentCarType)
            .where(
                dispatch.status.eq(DispatchStatus.WAITING),
                dispatch.adminGroup.id.eq(
                    JPAExpressions.select(adminGroup.id)
                        .from(user)
                        .leftJoin(user.adminGroup, adminGroup)
                        .where(user.id.eq(id))
                )
            )
            .fetch()
    }

    override fun getDispatchRequestPageList(
        pageable: Pageable,
        dispatchRequestSearchDto: DispatchRequestSearchDto,
        userId: Long
    ): Page<DispatchRequestResponseDto> {
        val user1 = QUser("user1")
        val query = queryFactory.select(
            Projections.constructor(
                DispatchRequestResponseDto::class.java,
                dispatch.id,
                dispatch.startAddress,
                dispatch.endAddress,
                dispatch.itemName,
                dispatch.itemCount,
                dispatch.palletCount,
                dispatch.paymentType,
                dispatch.originalPrice,
                dispatch.franchisee.companyName,
                dispatch.franchisee.managerPhone,
                dispatch.loadingMethod,
                dispatch.addressType,
                dispatch.createdDate,
                JPAExpressions.select(
                    Expressions.stringTemplate(
                        "GROUP_CONCAT({0})",
                        dispatchCarType.carType.name
                    )
                ).from(dispatchCarType)
                    .where(dispatchCarType.dispatch.id.eq(dispatch.id))
                    .leftJoin(dispatchCarType.carType)
            )
        ).from(dispatch)
            .where(
                dispatch.status.eq(DispatchStatus.REQUESTED),
                user.adminGroup.id.eq(
                    JPAExpressions.select(user1.adminGroup.id)
                        .from(user1)
                        .where(user1.id.eq(userId))
                )
            )
            .leftJoin(dispatch.franchisee)
            .leftJoin(dispatch.user, user)
            .orderBy(dispatch.id.desc())
            .limit(pageable.pageSize.toLong())
            .offset(pageable.offset.toLong())
            .fetch()

        val count = queryFactory.select(dispatch.id.count())
            .from(dispatch)
            .leftJoin(dispatch.user, user)
            .where(
                dispatch.status.eq(DispatchStatus.REQUESTED),
                user.adminGroup.id.eq(
                    JPAExpressions.select(user1.adminGroup.id)
                        .from(user1)
                        .where(user1.id.eq(userId))
                )
            )
            .fetchFirst()

        return PageableExecutionUtils.getPage(query, pageable) { count }
    }

    override fun getDispatchListByStatusAndDriver(status: DispatchStatus, driverId: Long): List<DispatchResponseDto>? {
        return queryFactory.select(defaultSelect())
            .from(dispatch)
            .leftJoin(dispatch.franchisee)
            .leftJoin(dispatch.driver)
            .leftJoin(dispatch.parentCarType)
            .where(status(status), dispatch.driver.id.eq(driverId))
            .orderBy(dispatch.dispatchDateTime.desc())
            .fetch()
    }

    override fun getActiveDispatchList(driverId: Long): List<DispatchResponseDto>? {
        return queryFactory.select(defaultSelect())
            .from(dispatch)
            .leftJoin(dispatch.franchisee)
            .leftJoin(dispatch.driver)
            .leftJoin(dispatch.parentCarType)
            .where(
                dispatch.driver.id.eq(driverId),
                dispatch.status.eq(DispatchStatus.DISPATCHED)
                    .or(dispatch.status.eq(DispatchStatus.LOADING))
            )
            .orderBy(dispatch.dispatchDateTime.desc())
            .fetch()
    }

    override fun getDispatchCount(driverId: Long?, status: DispatchStatus): Long? {
        return queryFactory.select(dispatch.id.count())
            .from(dispatch)
            .where(findDriver(driverId), status(status))
            .fetchOne()
    }

    override fun getCheckDispatch(id: Long, driverId: Long): DispatchCheckDto? {
        return queryFactory.select(
            QDispatchCheckDto(
                dispatch.status,
                dispatch.dispatchDateTime,
                dispatch.driverPrice
            )
        ).from(dispatch)
            .where(dispatch.id.eq(id)
                .and(dispatch.driver.id.eq(driverId))
                .and(dispatch.status.eq(DispatchStatus.DISPATCHED)
                    .or(dispatch.status.eq(DispatchStatus.LOADING))
                )
            )
            .fetchFirst()
    }

    override fun isDriverRunningBatch(driverId: Long, franchiseeId: Long): Boolean {
        return queryFactory.select(dispatch.id.count())
            .from(dispatch)
            .leftJoin(dispatch.franchisee)
            .leftJoin(dispatch.driver)
            .where(dispatch.driver.id.eq(driverId)
                .and(dispatch.franchisee.id.eq(franchiseeId))
                .and(dispatch.status.eq(DispatchStatus.DISPATCHED)
                    .or(dispatch.status.eq(DispatchStatus.LOADING))
                )
            ).fetchFirst() > 0
    }

    override fun getDispatchAdminGroupCode(id: Long): String? {
        return queryFactory.select(adminGroup.groupCode)
            .from(dispatch)
            .leftJoin(dispatch.adminGroup, adminGroup)
            .where(dispatch.id.eq(id))
            .fetchFirst()
    }

    override fun getFranchiseeId(id: Long): Long {
        return queryFactory.select(dispatch.franchisee.id)
            .from(dispatch)
            .where(dispatch.id.eq(id))
            .fetchOne() ?: throw BaseException(ErrorCode.ENTITY_NOT_FOUND)
    }

    override fun getFranchiseeDispatchDriverIdList(franchiseeId: Long): List<Long>? {
        return queryFactory.select(dispatch.driver.id)
            .from(dispatch)
            .where(dispatch.franchisee.id.eq(franchiseeId), dispatch.status.eq(DispatchStatus.DISPATCHED))
            .fetch()
    }

//    val id : Long,
//    val status: DispatchStatus,
//    val startSigungu: String,
//    var startSido: String,
//    val endSigungu: String,
//    var endSido: String,
//    val distance: Double,
//    val itemName: String
    override fun getFranchiseeMainScreenDispatchList(franchiseeId: Long): List<FranchiseeDispatchMainScreenResponseDto>? {
        return queryFactory.select(
            Projections.constructor(
                FranchiseeDispatchMainScreenResponseDto::class.java,
                dispatch.id,
                dispatch.status,
                dispatch.startSigungu,
                dispatch.startSido,
                dispatch.endSigungu,
                dispatch.endSido,
                dispatch.distance,
                dispatch.itemName
            )
        ).from(dispatch)
            .where(dispatch.franchisee.id.eq(franchiseeId))
            .orderBy(dispatch.id.desc())
            .limit(5)
            .fetch()
    }

    private fun search(dispatchSearchDto: DispatchSearchDto) : BooleanExpression? {
        if(dispatchSearchDto.searchWord.isNullOrEmpty()){
            return null
        }
        return when(dispatchSearchDto.searchType){
            DispatchSearchType.START_ADDRESS -> dispatch.startAddress.contains(dispatchSearchDto.searchWord)
            DispatchSearchType.END_ADDRESS -> dispatch.endAddress.contains(dispatchSearchDto.searchWord)
            DispatchSearchType.START_ADDRESS_AND_END_ADDRESS -> dispatch.startAddress.contains(dispatchSearchDto.searchWord)
                .or(dispatch.endAddress.contains(dispatchSearchDto.searchWord))
//            DispatchSearchType.ITEM_NAME -> dispatch.itemName.contains(dispatchSearchDto.searchWord)
//            DispatchSearchType.FRANCHISEE_NAME -> dispatch.franchisee.companyName.contains(dispatchSearchDto.searchWord)
//            DispatchSearchType.DRIVER_NAME -> dispatch.driver.name.contains(dispatchSearchDto.searchWord)
//            DispatchSearchType.DRIVER_PHONE -> dispatch.driver.phone.contains(dispatchSearchDto.searchWord)
//            DispatchSearchType.DRIVER_CAR_NUMBER -> dispatch.driver.carNumber.contains(dispatchSearchDto.searchWord)
//            DispatchSearchType.DRIVER_ID -> dispatch.driver.id.eq(dispatchSearchDto.searchUserId)
            else -> null
        }
    }

    private fun findDriver(id: Long?) : BooleanExpression? {
        if (id != null) {
            return dispatch.driver.id.eq(id)
        }
        return null
    }

    private fun statusList(statusList: List<DispatchStatus>?) : BooleanExpression? {
        if (statusList == null) {
            return null
        }
        return dispatch.status.`in`(statusList)
    }

    private fun status(status: DispatchStatus?) : BooleanExpression? {
        if (status == null) {
//            return (dispatch.status.ne(DispatchStatus.REQUESTED)).and(dispatch.status.ne(DispatchStatus.PENDING))
            return null
        }
        if (status == DispatchStatus.DISPATCHED) {
            return (dispatch.status.eq(DispatchStatus.DISPATCHED)
                .or(dispatch.status.eq(DispatchStatus.LOADING)))
        }
        return dispatch.status.eq(status)
    }

    private fun paymentType(paymentType: PaymentType?) : BooleanExpression? {
        if (paymentType == null) {
            return null
        }
        return dispatch.paymentType.eq(paymentType)
    }

    private fun searchFranchisee(franchiseeId: Long?) : BooleanExpression? {
        return franchiseeId?.let { dispatch.franchisee.id.eq(it) }
    }

    private fun searchCase(dispatchSearchDto: DispatchSearchDto) : BooleanExpression?{
        return when(dispatchSearchDto.case){
            DispatchSearchCase.COMPANY -> {
                val user1 = QUser("user")
                return user.adminGroup.id.eq(
                    JPAExpressions.select(user1.adminGroup.id)
                        .from(user1)
                        .where(user1.id.eq(dispatchSearchDto.searchUserId))
                )
            }
            DispatchSearchCase.FRANCHISEE -> {
                return dispatch.franchisee.id.eq(dispatchSearchDto.searchUserId)
            }
            else -> {
                return null
            }
        }
    }

    override fun test(id: Long): ApprovalUserTestDto? {
        return queryFactory.select(
            QApprovalUserTestDto(
                dispatchComplete.dispatch.finalApprovalUser.adminGroup.id,
                dispatchComplete.dispatch.finalApprovalUser.username
            )
        ).from(dispatchComplete)
            .leftJoin(dispatchComplete.dispatch.finalApprovalUser)
            .where(dispatchComplete.dispatch.finalApprovalUser.id.eq(id))
            .fetchOne()
    }
}
