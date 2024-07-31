package com.ilogistic.delivery_admin_backend.notice.domain.dto

import com.ilogistic.delivery_admin_backend.notice.domain.entity.Notice
import com.querydsl.core.annotations.QueryProjection
import java.time.LocalDateTime
data class NoticeListResponseDto @QueryProjection constructor(
    var id: Long,
    var title: String,
    var createdDate: LocalDateTime,
){
    companion object{
        fun of(notice: Notice) : NoticeListResponseDto {
            return NoticeListResponseDto(
                id = notice.id!!,
                title = notice.title,
                createdDate = notice.createdDate!!,
            )
        }
    }
}
