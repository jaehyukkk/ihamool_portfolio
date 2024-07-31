package com.ilogistic.delivery_admin_backend.notice.domain.dto

import io.swagger.v3.oas.annotations.media.Schema

class NoticeSearchDto(
    @Schema(description = "등록 시작 날짜", defaultValue = "2023-09-13")
    var startDate: String? = "",
    @Schema(description = "등록 마지막 날짜", defaultValue = "2023-09-29")
    var endDate: String? = "",
    @Schema(description = "검색어", defaultValue = "")
    var searchWord: String? = ""
) {
}
