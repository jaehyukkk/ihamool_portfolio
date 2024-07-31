package com.ilogistic.delivery_admin_backend.user.domain.dto

import com.ilogistic.delivery_admin_backend.user.domain.entity.User
import com.ilogistic.delivery_admin_backend.user.enums.UserRole
import io.swagger.v3.oas.annotations.media.Schema
import javax.validation.constraints.Email
import javax.validation.constraints.Size

class SignupDto(

    @field:Size(min = 2, max = 40, message = "회원 아이디는 2자 이상 20자 이하여야합니다.")
    @Schema(description = "회원 ID", defaultValue = "admin")
    var username: String,

    @field:Size(min = 2, max = 40, message = "비밀번호는 2자 이상 40자 이하여야합니다.")
    @Schema(description = "패스워드", defaultValue = "1234")
    var password: String,


//    @field:Size(min = 2, max = 20, message = "소속은 2자 이상 20자 이하여야합니다.")
//    @Schema(description = "소속", defaultValue = "개발팀")
//    var affiliation: String,
) {

    fun toEntity() : User {
        return User(
            username = username,
            password = password,
            userRole = UserRole.SUPER_ADMIN.value,
        )
    }
}
