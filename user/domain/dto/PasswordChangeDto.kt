package com.ilogistic.delivery_admin_backend.user.domain.dto

import io.swagger.v3.oas.annotations.media.Schema

class PasswordChangeDto(
    @Schema(description = "현재 비밀번호", defaultValue = "1111")
    var password : String? = null,
    @Schema(description = "변경 할 비밀번호", defaultValue = "1234")
    var newPassword : String
) {
}
