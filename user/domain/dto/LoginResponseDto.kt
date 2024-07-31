package com.ilogistic.delivery_admin_backend.user.domain.dto

class LoginResponseDto(
    val tokenResponse : TokenResponseDto,
    val userResponse: UserResponseDto
) {

    companion object{
        fun of(userResponseDto: UserResponseDto, tokenResponseDto: TokenResponseDto) : LoginResponseDto {
            return LoginResponseDto(
                    tokenResponse = tokenResponseDto,
                    userResponse = userResponseDto
            )
        }
    }
}
