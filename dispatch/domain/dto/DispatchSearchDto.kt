package com.ilogistic.delivery_admin_backend.dispatch.domain.dto

import com.ilogistic.delivery_admin_backend.dispatch.enums.DispatchSearchCase
import com.ilogistic.delivery_admin_backend.dispatch.enums.DispatchSearchType
import com.ilogistic.delivery_admin_backend.dispatch.enums.DispatchStatus
import com.ilogistic.delivery_admin_backend.dispatch.enums.PaymentType
import com.ilogistic.delivery_admin_backend.user.enums.UserRole
import com.ilogistic.delivery_admin_backend.utils.Utils
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.context.SecurityContextHolder

//startDate: '',
//endDate: '',
//paymentType: '',
//status: '',
class DispatchSearchDto(
    val startDate: String? = null,
    val endDate: String? = null,
    var paymentType: PaymentType? = null,
    var status: DispatchStatus? = null,
    var case: DispatchSearchCase? = null,
    var statusList: List<DispatchStatus>? = null,
    var searchType: DispatchSearchType? = null,
    var searchWord: String? = null,
    //검색한사람 아이디
    var searchUserId: Long? = null
) {
    init{
        val authentication = SecurityContextHolder.getContext().authentication
        if (Utils.roleCheck(authentication, listOf(UserRole.SUPER_ADMIN))) {
            case = DispatchSearchCase.SUPER
        } else if (Utils.roleCheck(authentication, listOf(UserRole.ADMIN, UserRole.CALL_WORKER))) {
            case = DispatchSearchCase.COMPANY
        } else if (Utils.roleCheck(authentication, listOf(UserRole.FRANCHISEE))) {
            case = DispatchSearchCase.FRANCHISEE
        }

        searchUserId = authentication.name.toLong()
    }
}
