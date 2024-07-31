package com.ilogistic.delivery_admin_backend.userpoint.domain.dto

import com.fasterxml.jackson.annotation.JsonIgnore
import com.ilogistic.delivery_admin_backend.userpoint.enums.PointReason
import com.ilogistic.delivery_admin_backend.userpoint.enums.PointType

class UserPointRequestDto(
    var userId: Long,
    var point: Int,
    @JsonIgnore
    var reason: PointReason? = null,
    @JsonIgnore
    var type: PointType? = null,
    var memo: String? = null,
) {
}
