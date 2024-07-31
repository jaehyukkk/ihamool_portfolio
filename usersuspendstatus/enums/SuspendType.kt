package com.ilogistic.delivery_admin_backend.usersuspendstatus.enums

enum class SuspendType(
    val description: String
) {
    //어플 결제 실패
    APP_PAYMENT_FAIL("어플 사용료 결제 실패"),
    //기타
    ETC("기타"),
}
