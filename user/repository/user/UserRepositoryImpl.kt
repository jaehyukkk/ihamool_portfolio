package com.ilogistic.delivery_admin_backend.user.repository.user

import com.ilogistic.delivery_admin_backend.admingroup.domain.dto.AdminGroupUserListResponseDto
import com.ilogistic.delivery_admin_backend.admingroup.domain.dto.AdminGroupUserSearchDto
import com.ilogistic.delivery_admin_backend.user.domain.dto.UserListResponseDto
import com.ilogistic.delivery_admin_backend.user.domain.dto.UserSearchDto
import com.ilogistic.delivery_admin_backend.admingroup.domain.entity.AdminGroup
import com.ilogistic.delivery_admin_backend.admingroup.domain.entity.QAdminGroup.adminGroup
import com.ilogistic.delivery_admin_backend.admingroup.enums.AdminGroupUserSearchType
import com.ilogistic.delivery_admin_backend.user.enums.UserSearchType
import com.ilogistic.delivery_admin_backend.user.domain.entity.QDriver.driver
import com.ilogistic.delivery_admin_backend.user.domain.entity.QFranchisee.franchisee
import com.ilogistic.delivery_admin_backend.user.domain.entity.QUser.user
import com.ilogistic.delivery_admin_backend.user.enums.UserRole
import com.ilogistic.delivery_admin_backend.userpoint.domain.entity.QUserPoint.userPoint
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
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime

@Repository
class UserRepositoryImpl(
    private val queryFactory: JPAQueryFactory
) : UserRepositoryCustom {


    override fun getPaymentTargetUserIdList(): List<Long> {

        // 현재 날짜에서 30일을 빼서 계산
        val thirtyDaysAgo = LocalDateTime.now().minusDays(31)

        return queryFactory
            .select(user.id)
            .from(user)
//            .leftJoin(applicationFeePayment).on(user.id.eq(applicationFeePayment.user.id))
            .where(
                user.userRole.`in`("ROLE_FRANCHISEE", "ROLE_DRIVER")
//                    .and(applicationFeePayment.createdDate.isNull.or(applicationFeePayment.createdDate.loe(thirtyDaysAgo)))
            )
            .fetch()
    }

    override fun getUserAdminGroupId(id: Long): Long? {
        return queryFactory.select(user.adminGroup.id)
            .from(user)
            .where(user.id.eq(id))
            .fetchFirst()
    }

    @Transactional
    override fun addUsersToAdminGroup(adminGroup: AdminGroup, userIds: List<Long>) {
        queryFactory.update(user)
            .set(user.adminGroup, adminGroup)
            .where(user.id.`in`(userIds))
            .execute()
    }

    @Transactional
    override fun addUserToAdminGroup(adminGroup: AdminGroup, userId: Long) {
        queryFactory.update(user)
            .set(user.adminGroup, adminGroup)
            .where(user.id.eq(userId))
            .execute()
    }

    override fun getUserAdminGroupCode(id: Long): String? {
        return queryFactory.select(adminGroup.groupCode)
            .from(user)
            .leftJoin(user.adminGroup, adminGroup)
            .where(user.id.eq(id))
            .fetchFirst()
    }

    override fun getUsersByAdminGroup(
        adminGroupId: Long,
        pageable: Pageable,
        adminGroupUserSearchDto: AdminGroupUserSearchDto
    ): Page<AdminGroupUserListResponseDto> {
        val query = queryFactory.select(
            Projections.constructor(
                AdminGroupUserListResponseDto::class.java,
                user.id,
                user.username,
                user.depositName,
                user.userRole,
                CaseBuilder()
                    .`when`(user.userRole.eq("ROLE_DRIVER")).then(driver.carNumber)
                    .otherwise(Expressions.asString("")),
                CaseBuilder()
                    .`when`(user.userRole.eq("ROLE_DRIVER")).then(driver.companyName)
                    .otherwise(CaseBuilder()
                        .`when`(user.userRole.eq("ROLE_FRANCHISEE")).then(franchisee.companyName)
                    .otherwise(user.adminGroup.companyName)),
                CaseBuilder()
                    .`when`(user.userRole.eq("ROLE_DRIVER")).then(driver.companyNumber)
                    .otherwise(CaseBuilder()
                        .`when`(user.userRole.eq("ROLE_FRANCHISEE")).then(franchisee.companyNumber)
                        .otherwise(user.adminGroup.companyNumber)),
            )
        ).from(user)
            .leftJoin(user.adminGroup, adminGroup)
            .leftJoin(driver).on(user.id.eq(driver.id))
            .leftJoin(franchisee).on(user.id.eq(franchisee.id))
            .where(
                searchRole(adminGroupUserSearchDto.role),
                adminGroup.id.eq(adminGroupId),
                adminGroupUserSearch(adminGroupUserSearchDto)
            )
            .offset(pageable.offset)
            .limit(pageable.pageSize.toLong())
            .orderBy(user.userRole.asc(), user.id.desc())
            .fetch()

        val count = queryFactory.select(user.id.count())
            .from(user)
            .leftJoin(user.adminGroup, adminGroup)
            .leftJoin(driver).on(user.id.eq(driver.id))
            .leftJoin(franchisee).on(user.id.eq(franchisee.id))
            .where(
                searchRole(adminGroupUserSearchDto.role),
                adminGroup.id.eq(adminGroupId),
                adminGroupUserSearch(adminGroupUserSearchDto)
            )
            .fetchFirst()

        return PageableExecutionUtils.getPage(query, pageable) { count }

    }

    @Transactional
    override fun deleteUserFromAdminGroup(adminGroup: AdminGroup, userId: Long) : Long {
        return queryFactory.update(user)
            .setNull(user.adminGroup)
            .where(user.id.eq(userId), user.adminGroup.eq(adminGroup), user.userRole.ne("ROLE_ADMIN"))
            .execute()
    }

    @Transactional
    override fun deleteUsersFromAdminGroup(adminGroup: AdminGroup, userIds: List<Long>): Long {
        return queryFactory.update(user)
            .setNull(user.adminGroup)
            .where(user.id.`in`(userIds), user.adminGroup.eq(adminGroup), user.userRole.ne("ROLE_ADMIN"))
            .execute()
    }

    override fun getUserList(userSearchDto: UserSearchDto, pageable: Pageable): Page<UserListResponseDto> {
        val query = queryFactory.select(
            Projections.constructor(
                UserListResponseDto::class.java,
                user.id,
                user.username,
                user.depositName,
                user.userRole,
                CaseBuilder()
                    .`when`(user.userRole.eq("ROLE_DRIVER")).then(driver.carNumber)
                    .otherwise(Expressions.asString("")),
                CaseBuilder()
                    .`when`(user.userRole.eq("ROLE_DRIVER")).then(driver.companyName)
                    .otherwise(CaseBuilder()
                        .`when`(user.userRole.eq("ROLE_FRANCHISEE")).then(franchisee.companyName)
                        .otherwise(CaseBuilder()
                            .`when`(user.userRole.eq("ROLE_SUPER_ADMIN")).then(Expressions.asString(""))
                            .otherwise(user.adminGroup.companyName))),
                CaseBuilder()
                    .`when`(user.userRole.eq("ROLE_DRIVER")).then(driver.companyNumber)
                    .otherwise(CaseBuilder()
                        .`when`(user.userRole.eq("ROLE_FRANCHISEE")).then(franchisee.companyNumber)
                        .otherwise(CaseBuilder()
                            .`when`(user.userRole.eq("ROLE_SUPER_ADMIN")).then(Expressions.asString(""))
                            .otherwise(user.adminGroup.companyNumber))),
                JPAExpressions.select(userPoint.currentPoint.coalesce(0))
                    .from(userPoint)
                    .where(userPoint.user.id.eq(user.id),
                        userPoint.id.eq(
                            JPAExpressions.select(userPoint.id.max())
                                .from(userPoint)
                                .where(userPoint.user.id.eq(user.id))

                        )
                    )
            )
        ).from(user)
            .leftJoin(user.adminGroup, adminGroup)
            .leftJoin(driver).on(user.id.eq(driver.id))
            .leftJoin(franchisee).on(user.id.eq(franchisee.id))
            .where(
                searchArrayRole(userSearchDto.roles),
                adminGroupType(userSearchDto.adminGroupType),
                userSearch(userSearchDto))
            .offset(pageable.offset)
            .limit(pageable.pageSize.toLong())
            .orderBy(user.userRole.asc(), user.id.desc())
            .fetch()

        val count = queryFactory.select(user.id.count())
            .from(user)
            .leftJoin(user.adminGroup, adminGroup)
            .leftJoin(driver).on(user.id.eq(driver.id))
            .leftJoin(franchisee).on(user.id.eq(franchisee.id))
            .where(
                searchArrayRole(userSearchDto.roles),
                adminGroupType(userSearchDto.adminGroupType),
                userSearch(userSearchDto)
            )
            .fetchFirst()

        return PageableExecutionUtils.getPage(query, pageable) { count }
    }

    private fun adminGroupType(adminGroupType: String?): BooleanExpression? {
        if(adminGroupType.isNullOrBlank()) {
            return null
        }
        if(adminGroupType == "1") {
            return user.adminGroup.isNotNull
        }
        return if(adminGroupType == "2") {
            user.adminGroup.isNull
        } else {
            null
        }
    }

    private fun searchRole(role: UserRole?): BooleanExpression? {
        if(role == null) {
            return null
        }
        return user.userRole.eq(role.value)
    }


    private fun searchArrayRole(roles: List<UserRole>?): BooleanExpression? {
        if(roles.isNullOrEmpty()) {
            return null
        }
        return user.userRole.`in`(*roles.map{it.value}.toTypedArray())
    }

    private fun userSearch(userSearchDto: UserSearchDto): BooleanExpression? {
        if(userSearchDto.searchWord.isNullOrBlank()) {
            return null
        }
        return when (userSearchDto.searchType) {
            UserSearchType.USER_NAME -> user.username.contains(userSearchDto.searchWord)
            UserSearchType.NAME -> user.depositName.contains(userSearchDto.searchWord)
            else -> throw IllegalArgumentException("검색 옵션을 확인해주세요.")
        }
    }

    private fun adminGroupUserSearch(adminGroupUserSearchDto: AdminGroupUserSearchDto): BooleanExpression? {
        if(adminGroupUserSearchDto.searchWord.isNullOrBlank()) {
            return null
        }
        return when (adminGroupUserSearchDto.searchType) {
            AdminGroupUserSearchType.USER_NAME -> user.username.contains(adminGroupUserSearchDto.searchWord)
            AdminGroupUserSearchType.NAME -> user.depositName.contains(adminGroupUserSearchDto.searchWord)
            else -> throw IllegalArgumentException("검색 옵션을 확인해주세요.")
        }
    }
    //    override fun getPaymentTargetUserList(): List<User> {
//        return queryFactory.select(user)
//            .from(user)
//            .leftJoin()
//    }

    //    override fun getUserPage(pageable: Pageable, userSearchDto: UserSearchDto): Page<UserListResponseDto> {
//        val fetch = queryFactory.select(
//            com.ilogistic.delivery_admin_backend.user.domain.dto.QUserListResponseDto(
//                user.id, user.username, user.name, user.phone, user.email
////            , user.affiliation
//                , user.createdDate
//            )
//        ).from(user)
//            .where(QuerydslUtil.betweenDate(user.createdDate, userSearchDto.startDate, userSearchDto.endDate), search(userSearchDto))
//            .orderBy(user.id.desc())
//            .offset(pageable.offset)
//            .limit(pageable.pageSize.toLong())
//            .fetch()
//
//        val count = queryFactory.select(user.id.count())
//            .from(user)
//            .where(QuerydslUtil.betweenDate(user.createdDate, userSearchDto.startDate, userSearchDto.endDate), search(userSearchDto))
//            .fetchFirst()
//
//        return PageableExecutionUtils.getPage(fetch, pageable) {count}
//    }

//    private fun search(userSearchDto: UserSearchDto) : BooleanExpression? {
//        if (!userSearchDto.searchWord.isNullOrBlank()) {
//            if (userSearchDto.searchOption.isNullOrBlank()) {
//                throw CustomMessageRuntimeException(ErrorCode.BAD_REQUEST, "검색 옵션을 확인해주세요.")
//            }
//            return when (userSearchDto.searchOption) {
//                "1" -> user.username.contains(userSearchDto.searchWord)
//                "2" -> user.name.contains(userSearchDto.searchWord)
//                "3" -> user.phone.contains(userSearchDto.searchWord)
//                "4" -> user.email.contains(userSearchDto.searchWord)
////                "5" -> user.affiliation.contains(userSearchDto.searchWord)
//                else -> throw CustomMessageRuntimeException(ErrorCode.BAD_REQUEST, "검색 옵션을 확인해주세요.")
//            }
//        }
//        return null
//    }
}
