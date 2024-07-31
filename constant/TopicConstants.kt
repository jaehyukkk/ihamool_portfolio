package com.ilogistic.delivery_admin_backend.constant

object TopicConstants {
    //새로운 오더
    const val NEW_ORDER_TOPIC = "new"
    //오더 배정
    const val DISPATCH_ORDER_TOPIC = "dispatch"
    //잡았다가 취소
    const val DISPATCH_CANCEL_TOPIC = "dispatch_cancel"
    //오더 완료
    const val COMPLETE_ORDER_TOPIC = "complete"
    //오더 수정
    const val MODIFY_ORDER_TOPIC = "update"
    //오더 삭제
    const val DELETE_ORDER_TOPIC = "delete"

    const val REQUEST_ORDER_TOPIC = "request_order"

    const val DRIVER_LOCATION_TOPIC = "driver_location"

    const val MESSAGE_TOPIC = "message"

    fun getTopic(orderStatus: String): String {
        return when (orderStatus) {
            "new" -> NEW_ORDER_TOPIC
            "dispatch" -> DISPATCH_ORDER_TOPIC
            "dispatch_cancel" -> DISPATCH_CANCEL_TOPIC
            "complete" -> COMPLETE_ORDER_TOPIC
            "update" -> MODIFY_ORDER_TOPIC
            "delete" -> DELETE_ORDER_TOPIC
            "driver_location" -> DRIVER_LOCATION_TOPIC
            else -> ""
        }
    }
}
