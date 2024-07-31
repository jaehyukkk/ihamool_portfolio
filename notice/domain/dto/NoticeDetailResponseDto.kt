package com.ilogistic.delivery_admin_backend.notice.domain.dto

import com.ilogistic.delivery_admin_backend.user.domain.dto.UserResponseDto
import com.ilogistic.delivery_admin_backend.notice.domain.entity.Notice
import com.querydsl.core.annotations.QueryProjection
import java.time.LocalDateTime

data class NoticeDetailResponseDto @QueryProjection constructor(
    val id : Long,
    val title : String,
    val content : String,
    val createdDate : LocalDateTime? = null,
    val updatedDate : LocalDateTime? = null,
    val hit: Int,
) {

    var prevId : Long? = null
    var prevTitle : String? = null
    var nextId : Long? = null
    var nextTitle : String? = null
}
