package com.ilogistic.delivery_admin_backend.utils

import java.time.LocalDateTime
import java.time.temporal.ChronoUnit

object TimeUtil {

    fun isNMinutesPassed(originalDateTime: LocalDateTime, n: Int): Boolean {
        // 현재 시간 가져오기
        val currentDateTime: LocalDateTime = LocalDateTime.now()

        // originalDateTime으로부터 현재 시간까지의 시간 차이 계산
        val minutesPassed = ChronoUnit.MINUTES.between(originalDateTime, currentDateTime)

        // N분이 지났는지 확인
        return minutesPassed >= n
    }
}
