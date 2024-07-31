package com.ilogistic.delivery_admin_backend.admingroup.domain.dto

import com.ilogistic.delivery_admin_backend.admingroup.enums.AdminGroupUserSearchType
import com.ilogistic.delivery_admin_backend.user.enums.UserRole
import com.ilogistic.delivery_admin_backend.user.enums.UserSearchType

class AdminGroupUserSearchDto(
    val searchType: AdminGroupUserSearchType? = null,
    val role: UserRole? = null,
    val searchWord: String? = null,
) {
}
