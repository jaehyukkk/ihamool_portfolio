package com.ilogistic.delivery_admin_backend.redis.service

import com.fasterxml.jackson.databind.ObjectMapper
import com.ilogistic.delivery_admin_backend.redis.handler.RedisMessageHandler
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.data.redis.core.ScanOptions
import org.springframework.data.redis.core.ValueOperations
import org.springframework.stereotype.Service
import org.springframework.web.socket.TextMessage
import org.springframework.web.socket.WebSocketSession
import java.util.concurrent.TimeUnit


@Service
class RedisService(
    private val redisTemplate: RedisTemplate<String, Any>,
) {
    companion object {
        private const val CHANNEL = "channel-1"
    }


    fun getValues(key: String): Any? {
        //opsForValue : Strings를 쉽게 Serialize / Deserialize 해주는 Interface
        val values: ValueOperations<String, Any> = redisTemplate.opsForValue()
        return values[key]
    }

    //클래스타입에 직렬화 할 DTO 클래스를 넣어주면, 해당 클래스타입으로 반환
    fun <T> getObjectValue(key: String, classType: Class<T>): T? {
        val values: String? = redisTemplate.opsForValue().get(key) as String?
        if (values.isNullOrBlank()) {
            return null
        }
        val mapper = ObjectMapper()
        return mapper.readValue(values, classType)
    }

    fun getKeysByPattern(pattern: String): Set<String> {
        val scanOptions = ScanOptions.scanOptions().match("*$pattern*").build()
        val keys = mutableSetOf<String>()

        redisTemplate.execute { connection ->
            val cursor = connection.scan(scanOptions)
            while (cursor.hasNext()) {
                keys.add(String(cursor.next()))
            }
            return@execute keys
        }

        return keys
    }

    fun <T> getObjectListByPattern(pattern: String, classType: Class<T>): List<T> {
        val keys = getKeysByPattern(pattern)
        val result = mutableListOf<T>()
        for (key in keys) {
            val obj = getObjectValue(key, classType)
            if (obj != null) {
                result.add(obj)
            }
        }
        return result
    }

    fun setObjectValues(key: String, value: Any) {
        val values: ValueOperations<String, Any> = redisTemplate.opsForValue()
        val mapper = ObjectMapper()
        values[key] = mapper.writeValueAsString(value)
    }

    fun setValues(key: String, value: Any) {
        val values: ValueOperations<String, Any> = redisTemplate.opsForValue()
        values[key] = value
    }

    fun setValues(key: String, value: Any, timeout: Long, timeUnit: TimeUnit?) {
        val values: ValueOperations<String, Any> = redisTemplate.opsForValue()
        values[key, value, timeout] = timeUnit!!
    }

    fun delete(key: String) {
        val values: ValueOperations<String, Any> = redisTemplate.opsForValue()
        values[key, "", 1] = TimeUnit.MICROSECONDS
    }

    /**
     * 메시지 발행
     * @param message 메시지
     */
    fun publish(channel: String, message: String) {
        redisTemplate.convertAndSend(channel, message)
    }

    /**
     * 메시지 구독
     * @param session WebSocketSession
     */
    fun subscribe(session: WebSocketSession) {
        redisTemplate.connectionFactory?.connection?.subscribe(getMessageHandler(session), CHANNEL.toByteArray())
    }

    /**
     * 여러 채널 메시지 구독
     * @param session WebSocketSession
     */
    fun subscribe(channel: Array<String>, session: WebSocketSession) {
        for (c in channel) {
            redisTemplate.connectionFactory?.connection?.subscribe(getMessageHandler(session), c.toByteArray())
        }
    }

    /**
     * 메세지 핸들러 생성
     * @param session WebSocketSession
     */
    private fun getMessageHandler(session: WebSocketSession): RedisMessageHandler {
        return RedisMessageHandler(session)
    }
}
