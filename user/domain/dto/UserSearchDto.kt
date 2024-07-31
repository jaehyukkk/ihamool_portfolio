package com.ilogistic.delivery_admin_backend.user.domain.dto

import com.ilogistic.delivery_admin_backend.user.enums.UserSearchType
import com.ilogistic.delivery_admin_backend.user.enums.UserRole

class UserSearchDto(
    val searchType: UserSearchType? = null,
    val roles: List<UserRole> = listOf(),
    val searchWord: String? = null,
    //1 소속, 2 미소속
    val adminGroupType: String? = null,
) {
}
