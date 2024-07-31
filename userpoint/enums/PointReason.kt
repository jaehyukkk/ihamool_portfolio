package com.ilogistic.delivery_admin_backend.userpoint.enums

enum class PointReason {
    //직접 충전
    SELF_CHARGE,
    //어플 이용료
    APPLICATION_USE_FEE,
    //관리자 지급
    ADMIN_CHARGE,
    //관리자 차감
    ADMIN_DEDUCT,
    //세금계산서
    TAX_INVOICE,
}
