package com.ilogistic.delivery_admin_backend.notice.domain.dto

import com.querydsl.core.annotations.QueryProjection

data class SubPostDto @QueryProjection constructor(
    var id: Long,
    val title: String,
) {
}
