package com.ilogistic.delivery_admin_backend.user.domain.dto

import io.swagger.v3.oas.annotations.media.Schema

class PasswordChangeDtoV2(
    @Schema(description = "변경 할 비밀번호", defaultValue = "1234")
    var password : String
) {
}
