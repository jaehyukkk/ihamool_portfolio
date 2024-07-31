package com.ilogistic.delivery_admin_backend.handler
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.google.gson.Gson
import com.ilogistic.delivery_admin_backend.dispatch.domain.dto.WebSocketSendDto
import com.ilogistic.delivery_admin_backend.dispatch.enums.WebSocketTopic
import com.ilogistic.delivery_admin_backend.dto.common.LocationRequestDto
import com.ilogistic.delivery_admin_backend.dto.common.LocationResponseDto
import com.ilogistic.delivery_admin_backend.exception.BaseException
import com.ilogistic.delivery_admin_backend.exception.ErrorCode
import com.ilogistic.delivery_admin_backend.jwt.TokenProvider
import com.ilogistic.delivery_admin_backend.redis.service.RedisService
import com.ilogistic.delivery_admin_backend.user.enums.UserRole
import com.ilogistic.delivery_admin_backend.user.service.UserService
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Component
import org.springframework.web.socket.TextMessage
import org.springframework.web.socket.WebSocketSession
import org.springframework.web.socket.handler.TextWebSocketHandler
import java.io.IOException
import java.util.concurrent.ConcurrentHashMap

@Component
class WebSocketHandler(
    private val tokenProvider: TokenProvider,
    private val redisService: RedisService,
    private val userService: UserService
) : TextWebSocketHandler() {

    companion object {
        private val sessions = ConcurrentHashMap<String, WebSocketSession>()
    }

    @Throws(Exception::class)
    override fun afterConnectionEstablished(session: WebSocketSession) {
        val accessToken = session.uri?.query?.split("=")?.get(1)
            ?: session.handshakeHeaders.getFirst("Authorization")
            ?: throw BaseException(ErrorCode.UNAUTHORIZED)

//        sessions[session.id] = session
        tokenProvider.validateToken(accessToken)

        val claims = tokenProvider.parseClaims(accessToken)
        val userId = claims.subject.toLong()
        val role = claims["auth"].toString()

        session.attributes["userId"] = userId
        session.attributes["role"] = role

        val channels: MutableList<String> = mutableListOf("default-1")

        if(role.contains(UserRole.DRIVER.value)){
            val carTypeId = userService.getDriverCarTypeId(userId)
            channels.add("driver-1")
            channels.add("driver-c-${carTypeId}")
        }
        if(role.contains(UserRole.FRANCHISEE.value)){
            channels.add("franchisee-1")
        }
        if(role.contains(UserRole.ADMIN.value) || role.contains(UserRole.CALL_WORKER.value)){
            val groupCode = userService.getUserAdminGroupCode(userId)
            channels.add("admin-1")
            channels.add("admin-g-${groupCode}")
        }
        if (role.contains(UserRole.SUPER_ADMIN.value)) {
            channels.add("super-admin-1")
        }

        println("channels😅 : $channels")
        redisService.subscribe(channels.toTypedArray(), session)
    }

//    @Throws(Exception::class)
//    override fun afterConnectionClosed(session: WebSocketSession, status: CloseStatus) {
//        sessions.remove(session.id)
//    }

    override fun handleTextMessage(session: WebSocketSession, message: TextMessage) {
        val gson = Gson()
        val jsonObject = gson.fromJson(message.payload, Any::class.java) as Map<*, *>
        if (jsonObject["topic"].toString() == WebSocketTopic.DRIVER_LOCATION_TOPIC.name) {
            //locationDto로 변환
            val locationRequestDto : LocationRequestDto = gson.fromJson(jsonObject["data"].toString(), LocationRequestDto::class.java)
//            val driver = redisService.getObjectValue("USER-${session.attributes["userId"]}", DriverRedisSaveDto::class.java)
            val locationResponseDto = LocationResponseDto.of(locationRequestDto)
            val channels : MutableList<WebSocketSendDto.Companion.Channel> = mutableListOf()

            channels.add(WebSocketSendDto.Companion.Channel(
                name = "admin-g-${locationRequestDto.driverAdminGroupCode}",
            ))
            channels.add(WebSocketSendDto.Companion.Channel(
                name = "super-admin-1",
            ))
            val dispatchWebSocketSendDto = WebSocketSendDto(
                data = locationResponseDto,
                topic = WebSocketTopic.DRIVER_LOCATION_TOPIC,
                channels = channels
            )
            sendMessage(dispatchWebSocketSendDto)
            //redis에 저장
            redisService.setObjectValues("LOCATION-${session.attributes["userId"]}", locationResponseDto)
        }
    }


    fun sendMessageToUsers(userIds: List<Long>, message: String) {
        // 세션 리스트에 있는 특정 유저에게 메시지를 전송합니다.
        sessions.forEach { session ->
            if (userIds.contains(session.value.attributes["userId"])) {
                try {
                    session.value.sendMessage(TextMessage(message))
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
        }
    }


    /**
     * 메시지 전송
     * @param webSocketSendDto WebSocketSendDto
     */
    @Async
    fun sendMessage(webSocketSendDto: WebSocketSendDto) {
        val mapper = ObjectMapper().registerModule(JavaTimeModule())
        webSocketSendDto.channels.forEach { channel ->
            webSocketSendDto.targetIds = channel.targetIds
            redisService.publish(channel.name, mapper.writeValueAsString(webSocketSendDto))
        }
    }

}
