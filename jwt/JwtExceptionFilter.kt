package com.ilogistic.delivery_admin_backend.jwt

import com.ilogistic.delivery_admin_backend.exception.ErrorCode
import com.ilogistic.delivery_admin_backend.exception.ErrorResponse
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import io.jsonwebtoken.JwtException
import io.jsonwebtoken.io.IOException
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter
import org.springframework.web.servlet.HandlerExceptionResolver
import javax.servlet.FilterChain
import javax.servlet.ServletException
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse


@Component
class JwtExceptionFilter(
    @Qualifier("handlerExceptionResolver")
    private val exceptionResolver : HandlerExceptionResolver
) : OncePerRequestFilter() {
    @Throws(ServletException::class, IOException::class)
    override fun doFilterInternal(req: HttpServletRequest, res: HttpServletResponse, chain: FilterChain) {
        try {
            chain.doFilter(req, res) // go to 'JwtAuthenticationFilter'
        } catch (ex: JwtException) {
            exceptionResolver.resolveException(req, res, null, ex)
//            setErrorResponse(HttpStatus.UNAUTHORIZED, res, ex)
        }
    }

    @Throws(IOException::class)
    fun setErrorResponse(status: HttpStatus, res: HttpServletResponse, ex: Throwable) {
        val mapper = ObjectMapper().registerKotlinModule()
        res.status = status.value()
        res.contentType = "application/json; charset=UTF-8"
        val jwtExceptionResponse = ErrorResponse(ErrorCode.UNAUTHORIZED, ex.message ?: "비정상적인 토큰입니다.")
        res.writer.write(mapper.writeValueAsString(jwtExceptionResponse))
    }
}
