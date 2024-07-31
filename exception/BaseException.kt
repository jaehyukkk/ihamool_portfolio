package com.ilogistic.delivery_admin_backend.exception

class BaseException(val errorCode: ErrorCode) : RuntimeException(errorCode.message)

