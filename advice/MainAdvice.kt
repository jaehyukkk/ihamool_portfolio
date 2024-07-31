package com.ilogistic.delivery_admin_backend.advice

import com.ilogistic.delivery_admin_backend.exception.*
import io.jsonwebtoken.JwtException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.validation.BindingResult
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class MainAdvice {

    @ExceptionHandler(CustomRuntimeException::class)
    fun customRuntimeException(ex: CustomRuntimeException): ResponseEntity<ErrorResponse> {
        val errorResponse = ErrorResponse(ex.errorCode)
        return ResponseEntity(errorResponse, HttpStatus.valueOf(ex.errorCode.status))
    }

    @ExceptionHandler(BaseException::class)
    fun customRuntimeException(ex: BaseException): ResponseEntity<ErrorResponse> {
        val errorResponse = ErrorResponse(ex.errorCode)
        return ResponseEntity(errorResponse, HttpStatus.valueOf(ex.errorCode.status))
    }

    @ExceptionHandler(CustomMessageRuntimeException::class)
    fun customMessageRuntimeException(ex: CustomMessageRuntimeException): ResponseEntity<ErrorResponse> {
        val errorResponse = ErrorResponse(ex.errorCode, ex.msg)
        return ResponseEntity(errorResponse, HttpStatus.valueOf(ex.errorCode.status))
    }

    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun methodArgumentNotValidException(
        ex: MethodArgumentNotValidException,
        bindingResult: BindingResult
    ): ResponseEntity<ErrorResponse> {
        val errorResponse =
            ErrorResponse(ErrorCode.BAD_REQUEST, bindingResult.fieldError?.defaultMessage ?: "valid error")
        return ResponseEntity(errorResponse, HttpStatus.valueOf(ErrorCode.BAD_REQUEST.status))
    }

    @ExceptionHandler(BadCredentialsException::class)
    fun badCredentialsException(ex : BadCredentialsException): ResponseEntity<ErrorResponse> {
        val errorResponse =
            ErrorResponse(ErrorCode.BAD_REQUEST,"아이디 혹은 비밀번호를 확인해주세요")
        return ResponseEntity(errorResponse, HttpStatus.valueOf(ErrorCode.BAD_REQUEST.status))
    }

    @ExceptionHandler(JwtException::class)
    fun jwtException(ex : JwtException): ResponseEntity<ErrorResponse> {
        val errorResponse = ErrorResponse(ErrorCode.UNAUTHORIZED,ex.message ?: "잘못된 토큰입니다.")
        return ResponseEntity(errorResponse, HttpStatus.valueOf(ErrorCode.UNAUTHORIZED.status))
    }
}
