package com.ilogistic.delivery_admin_backend.dispatch.domain.dto

import com.querydsl.core.annotations.QueryProjection

data class DispatchCountResponseDto @QueryProjection constructor(
    var waiting: Long,
    var dispatched: Long,
    var completed: Long
) {
}
