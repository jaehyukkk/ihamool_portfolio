package com.ilogistic.delivery_admin_backend.exception


class ErrorResponse(
        var status: Int = 0,
        var message: String? = null,
        var code: String? = null
) {
    constructor(errorCode: ErrorCode) : this(errorCode.status, errorCode.message, errorCode.errorCode)
    constructor(errorCode: ErrorCode, msg: String) : this(errorCode.status, msg, errorCode.errorCode)
}

