package com.ilogistic.delivery_admin_backend.dispatch.enums

enum class DispatchStatus(val stringValue: String, val value: String? = null){
    WAITING("WAITING"),
    DISPATCHED("DISPATCHED"),
    COMPLETED("COMPLETED"),
    CANCELLED("CANCELLED"),
    LOADING("LOADING"),
    //잡았다가 취소 (로깅용)
    DISPATCH_CANCELLED("DISPATCH_CANCELLED"),
    //보류
    PENDING("PENDING"),
    //요청
    REQUESTED("REQUESTED"),
    THROW("THROW");
    companion object{
        fun findDispatchStatus(status: String): DispatchStatus {
            return when(status) {
                "WAITING" -> WAITING
                "DISPATCHED" -> DISPATCHED
                "COMPLETED" -> COMPLETED
                "CANCELLED" -> CANCELLED
                "LOADING" -> LOADING
                "DISPATCH_CANCELLED" -> DISPATCH_CANCELLED
                "PENDING" -> PENDING
                "REQUESTED" -> REQUESTED
                "THROW" -> THROW
                else -> throw IllegalArgumentException("Invalid status")
            }
        }
    }

}
