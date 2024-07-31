package com.ilogistic.delivery_admin_backend.utils

import kotlin.math.*

object DistanceUtil {

    private const val EARTH_RADIUS = 6371 // 지구 반경 (킬로미터)

    fun getDistance(lat1: Double, lon1: Double, lat2: Double, lon2: Double): Double {
        val earthRadius = 6371 // 지구 반지름 (단위: 킬로미터)

        // 위도 및 경도를 라디안으로 변환
        val lat1Rad = Math.toRadians(lat1)
        val lon1Rad = Math.toRadians(lon1)
        val lat2Rad = Math.toRadians(lat2)
        val lon2Rad = Math.toRadians(lon2)

        // Haversine 공식을 사용하여 거리 계산
        val dlon = lon2Rad - lon1Rad
        val dlat = lat2Rad - lat1Rad
        val a = sin(dlat / 2).pow(2) + cos(lat1Rad) * cos(lat2Rad) * sin(dlon / 2).pow(2)
        val c = 2 * atan2(sqrt(a), sqrt(1 - a))

        return earthRadius * c
    }

    private fun toRadians(degrees: Double): Double {
        return degrees * (PI / 180)
    }

}
