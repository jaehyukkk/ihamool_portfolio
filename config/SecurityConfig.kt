package com.ilogistic.delivery_admin_backend.config

import com.ilogistic.delivery_admin_backend.jwt.*
import lombok.RequiredArgsConstructor
import org.springframework.context.annotation.Bean
import org.springframework.security.config.annotation.SecurityConfigurerAdapter
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.DefaultSecurityFilterChain
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.CorsConfigurationSource
import org.springframework.web.cors.UrlBasedCorsConfigurationSource

@RequiredArgsConstructor
@EnableWebSecurity
class SecurityConfig(
        private val tokenProvider: TokenProvider,
        private val jwtAccessDeniedHandler: JwtAccessDeniedHandler,
        private val jwtAuthenticationEntryPoint: JwtAuthenticationEntryPoint,
        private val jwtExceptionFilter: JwtExceptionFilter
) : WebSecurityConfigurerAdapter() {

    @Bean
    fun passwordEncoder(): PasswordEncoder {
        return BCryptPasswordEncoder()
    }

    @Throws(Exception::class)
    override fun configure(http: HttpSecurity) {
        http
                .httpBasic().disable()
                .csrf().disable()
                .exceptionHandling()
                .authenticationEntryPoint(jwtAuthenticationEntryPoint)
                .accessDeniedHandler(jwtAccessDeniedHandler)
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeHttpRequests() //                .antMatchers("/auth/**").permitAll()
                .antMatchers("/**").permitAll()
                .anyRequest().authenticated()
                .and()
                .apply<SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity>>(JwtSecurityConfig(tokenProvider, jwtExceptionFilter))
    }

//    @Bean
//    fun corsConfigurationSource(): CorsConfigurationSource {
//        val configuration = CorsConfiguration()
//        configuration.allowedOrigins = listOf("http://localhost:3001", "http://doallat.iptime.org:3001", "http://localhost:2607")
//        configuration.allowedMethods = listOf("GET", "POST", "PUT", "DELETE")
//        configuration.allowedHeaders = listOf("Origin", "X-Requested-With", "Content-Type", "Authorization", "Oauth-Token")
//        configuration.maxAge = 3000L
//        configuration.allowCredentials = true
//        val source = UrlBasedCorsConfigurationSource()
//        source.registerCorsConfiguration("/**", configuration)
//        return source
//    }

}
