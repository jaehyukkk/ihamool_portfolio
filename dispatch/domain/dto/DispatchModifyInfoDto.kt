package com.ilogistic.delivery_admin_backend.dispatch.domain.dto

import com.ilogistic.delivery_admin_backend.dispatch.enums.DispatchStatus
import com.querydsl.core.annotations.QueryProjection

data class DispatchModifyInfoDto @QueryProjection constructor(
    val dispatchStatus : DispatchStatus,
    val dispatchCode: String
) {
}
