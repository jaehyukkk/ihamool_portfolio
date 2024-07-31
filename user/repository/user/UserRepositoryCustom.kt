package com.ilogistic.delivery_admin_backend.user.repository.user

import com.ilogistic.delivery_admin_backend.admingroup.domain.dto.AdminGroupUserListResponseDto
import com.ilogistic.delivery_admin_backend.admingroup.domain.dto.AdminGroupUserSearchDto
import com.ilogistic.delivery_admin_backend.user.domain.dto.UserListResponseDto
import com.ilogistic.delivery_admin_backend.user.domain.dto.UserSearchDto
import com.ilogistic.delivery_admin_backend.admingroup.domain.entity.AdminGroup
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable

interface UserRepositoryCustom {
    //    fun getAdmin()
    fun getPaymentTargetUserIdList() : List<Long>

    fun addUsersToAdminGroup(adminGroup: AdminGroup, userIds: List<Long>)

    fun addUserToAdminGroup(adminGroup: AdminGroup, userId: Long)

    fun getUserAdminGroupId(id : Long) : Long?

    fun getUserAdminGroupCode(id : Long) : String?

    fun getUsersByAdminGroup(adminGroupId: Long, pageable: Pageable, adminGroupUserSearchDto: AdminGroupUserSearchDto): Page<AdminGroupUserListResponseDto>

    fun deleteUserFromAdminGroup(adminGroup: AdminGroup, userId: Long) : Long
    fun deleteUsersFromAdminGroup(adminGroup: AdminGroup, userIds: List<Long>) : Long

    fun getUserList(userSearchDto: UserSearchDto, pageable: Pageable): Page<UserListResponseDto>
}
