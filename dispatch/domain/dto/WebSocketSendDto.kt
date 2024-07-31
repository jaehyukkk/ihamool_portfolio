package com.ilogistic.delivery_admin_backend.dispatch.domain.dto

import com.ilogistic.delivery_admin_backend.dispatch.enums.WebSocketSubTopic
import com.ilogistic.delivery_admin_backend.dispatch.enums.WebSocketTopic

class WebSocketSendDto(
    var data: Any,
    var topic: WebSocketTopic,
    var subTopic: WebSocketSubTopic = WebSocketSubTopic.DEFAULT,
    var channels: MutableList<Channel> = mutableListOf(),
    var targetIds: MutableList<Long>? = mutableListOf()
) {

    companion object{
        data class Channel(
            val name: String,
            val targetIds: MutableList<Long>? = mutableListOf()
        ){}
    }
}
