package com.ilogistic.delivery_admin_backend.chargerequest.domain.dto

import com.ilogistic.delivery_admin_backend.chargerequest.enums.ChargeRequestStatus
import com.ilogistic.delivery_admin_backend.user.enums.UserRole

class ChargeRequestSearchDto(
    var startDate: String? = "",
    var endDate: String? = "",
    var searchType: String? = "",
    var searchWord: String? = "",
    var status: ChargeRequestStatus? = null,
    var role: UserRole? = null,
) {
}
