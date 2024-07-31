package com.ilogistic.delivery_admin_backend.user.domain.dto

import io.swagger.v3.oas.annotations.media.Schema

class ReissueDto(
        @Schema(description = "액세스 토큰")
        val accessToken: String,
        @Schema(description = "리프레쉬 토큰")
        val refreshToken: String,
) {
}
