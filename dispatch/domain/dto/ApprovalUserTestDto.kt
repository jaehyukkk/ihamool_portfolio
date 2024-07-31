package com.ilogistic.delivery_admin_backend.dispatch.domain.dto

import com.querydsl.core.annotations.QueryProjection

data class ApprovalUserTestDto @QueryProjection constructor(
    val id : Long,
    val username : String,
) {
}
