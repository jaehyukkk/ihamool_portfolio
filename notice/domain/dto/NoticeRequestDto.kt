package com.ilogistic.delivery_admin_backend.notice.domain.dto

import com.ilogistic.delivery_admin_backend.notice.domain.entity.Notice
import com.ilogistic.delivery_admin_backend.user.domain.entity.User
import io.swagger.v3.oas.annotations.media.Schema

class NoticeRequestDto(
    @Schema(description = "제목", defaultValue = "공지사항")
    var title : String = "",
    @Schema(description = "내용", defaultValue = "공지사항 내용")
    var content : String = "",
) {
    fun toEntity(user : User) : Notice {
        return Notice(
            title = title,
            content = content,
            user = user
        )
    }
}
