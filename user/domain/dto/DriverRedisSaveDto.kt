package com.ilogistic.delivery_admin_backend.user.domain.dto

import com.querydsl.core.annotations.QueryProjection

data class DriverRedisSaveDto @QueryProjection constructor(
    var id: Long? = null,
    var name: String? = null,
    var phone: String? = null,
    var carNumber: String? = null,
    var carTypeId: Long? = null,
) {
}
