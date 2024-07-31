package com.ilogistic.delivery_admin_backend.dto.common

import io.swagger.v3.oas.annotations.media.Schema
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable

class PaginateDto(

        @Schema(description = "페이지 (최소값 : 1)", defaultValue = "1")
        var page : Int = 1,

        @Schema(description = "한페이지당 게시글 수", defaultValue = "10")
        var size : Int = 10,
) {
    fun pageable(): Pageable {
        return PageRequest.of(page - 1, size)
    }
}



