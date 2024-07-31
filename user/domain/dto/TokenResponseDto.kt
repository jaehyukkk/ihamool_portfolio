package com.ilogistic.delivery_admin_backend.user.domain.dto

class TokenResponseDto(
        val grantType: String,
        val accessToken: String,
        val refreshToken: String? = null,
        val accessTokenExpiresIn: Long,
        val refreshTokenExpiresIn: Long? = null
) {
}
