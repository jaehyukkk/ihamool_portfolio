package com.ilogistic.delivery_admin_backend.user.domain.dto

import io.swagger.v3.oas.annotations.media.Schema
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import javax.validation.constraints.Size

class LoginRequestDto(
        @field:Size(min = 2, max = 40, message = "회원 아이디는 2자 이상 20자 이하여야합니다.")
        @Schema(description = "회원 ID", defaultValue = "admin")
        private var username: String,

        @field:Size(min = 2, max = 40, message = "비밀번호는 2자 이상 40자 이하여야합니다.")
        @Schema(description = "회원 PASSWORD", defaultValue = "1234")
        private var password: String
) {

    fun toAuthentication(): UsernamePasswordAuthenticationToken {
        return UsernamePasswordAuthenticationToken(username, password)
    }
}
