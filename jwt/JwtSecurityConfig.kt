package com.ilogistic.delivery_admin_backend.jwt

import lombok.RequiredArgsConstructor
import org.springframework.security.config.annotation.SecurityConfigurerAdapter
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.web.DefaultSecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter


@RequiredArgsConstructor
class JwtSecurityConfig(
    private val tokenProvider: TokenProvider,
    private val jwtExceptionFilter: JwtExceptionFilter
) : SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity>() {

    override fun configure(http: HttpSecurity) {
        val customFilter = JwtFilter(tokenProvider)
        http.addFilterBefore(customFilter, UsernamePasswordAuthenticationFilter::class.java)
        http.addFilterBefore(jwtExceptionFilter, JwtFilter::class.java)
    }
}

