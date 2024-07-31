package com.ilogistic.delivery_admin_backend.dispatch.enums

enum class LoadingMethod(
    private val value: String
) {
    FORKLIFT("지게차"),
    //호이스트
    HOIST("호이스트"),
    //크레인
    CRANE("크레인"),
    //수작업
    MANUAL("수작업"),
    //기타
    ETC("기타"),

    ;

    override fun toString(): String {
        return value
    }
}
