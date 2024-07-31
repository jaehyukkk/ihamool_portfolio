package com.ilogistic.delivery_admin_backend.utils

import com.ilogistic.delivery_admin_backend.user.enums.UserRole
import org.springframework.security.core.Authentication
import org.springframework.security.core.GrantedAuthority
import java.text.SimpleDateFormat
import java.util.*

object Utils {

    //주문번호
    fun generateDispatchCode(): String {
        // 현재 날짜를 가져오기
        val currentDate = Calendar.getInstance().time
        val dateFormat = SimpleDateFormat("yyMMdd", Locale.getDefault())
        val datePart = dateFormat.format(currentDate)

        // 대문자 영어와 숫자로 이루어진 6자리의 난수 생성
        val randomChars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789"
        val randomPart = (1..6)
            .map { randomChars.random() }
            .joinToString("")

        // yyMMdd 형식과 6자리의 난수 조합하여 8자리의 문자열 생성
        return "$datePart$randomPart"
    }



    fun getCarType(ton: Double) : Int {
        return if (ton < 2.5) {
            1; // 2.5 톤 미만 화물차
        } else if (ton in 2.5..5.5) {
            2; // 2.5~5.5 톤 화물차
        } else if (ton > 5.5 && ton <= 10) {
            3; // 5.5~10 톤 화물차
        } else if (ton > 10 && ton <= 20) {
            4; // 10~20 톤 화물차
        } else if (ton > 20) {
            5; // 20 톤 이상 화물차
        } else {
            1; // 분류 기준에 맞지 않는 경우
        }
    }

    private fun tonConvert(ton: Double) : Int {
        return when {
            ton < 2.5 -> 1
            //2.5이상 5.0미만
            ton < 5.0 -> 2
            //5.0이상
            else -> 5
        }
    }

    // 운송 거리와 차량 톤수를 입력 받아 배송 요금을 계산하는 함수
    fun calculateDeliveryFee(distance: Int, tonnage: Double): Int {
        val defaultDistanceRate = mapOf(
            1 to 50000, // 1톤 화물차: 50,000원
            2 to 70000, // 2.5톤 화물차: 70,000원
            5 to 100000 // 5톤 화물차: 100,000원
        )

        val additionalDistanceRate = mapOf(
            1 to 1000, // 1톤 화물차: km당 1,000원
            2 to 1500, // 2.5톤 화물차: km당 1,500원
            5 to 2000 // 5톤 화물차: km당 2,000원
        )

        val tonConvert = tonConvert(tonnage)
//        println("tonConvert: $tonConvert")
        val additionalDistanceThreshold = 50 // 50km 초과 시 추가 요금 적용

        val baseFee = defaultDistanceRate[tonConvert] ?: error("Invalid tonnage")
        val distanceRate = additionalDistanceRate[tonConvert] ?: error("Invalid tonnage")

        var totalFee = baseFee // 기본 요금

        // 추가 요금 적용
        if (distance > additionalDistanceThreshold) {
            val additionalDistance = distance - additionalDistanceThreshold
            totalFee += additionalDistance * distanceRate
        }

        return totalFee
    }

    fun roleCheck(authentication: Authentication, targetRole : List<UserRole>): Boolean {
        return authentication.authorities.stream()
            .map { r: GrantedAuthority? -> r!!.authority}
            .toList()
            .any { userRole -> targetRole.any { it.value == userRole }}
    }

    //대문자영문 숫자 조합 6자리 난수 생성
    fun generateRandomCode(): String {
        val randomChars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789"
        return (1..6)
            .map { randomChars.random() }
            .joinToString("")
    }



}
