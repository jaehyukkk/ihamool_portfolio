package com.ilogistic.delivery_admin_backend.exception

class CustomRuntimeException(val errorCode: ErrorCode) : RuntimeException(errorCode.message)

