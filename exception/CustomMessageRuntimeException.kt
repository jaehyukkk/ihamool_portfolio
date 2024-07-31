package com.ilogistic.delivery_admin_backend.exception

class CustomMessageRuntimeException(
        val errorCode: ErrorCode,
        val msg : String
) : RuntimeException(msg) {
}
