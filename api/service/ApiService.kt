package com.ilogistic.delivery_admin_backend.api.service

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import com.ilogistic.delivery_admin_backend.api.domain.dto.KakaoResponse
import com.ilogistic.delivery_admin_backend.exception.BaseException
import com.ilogistic.delivery_admin_backend.exception.ErrorCode
import com.ilogistic.delivery_admin_backend.utils.DistanceUtil
import com.ilogistic.delivery_admin_backend.utils.Utils
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.http.MediaType
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate
import org.springframework.web.util.UriComponentsBuilder

@Service
class ApiService(
    @Value("\${kakao.api.base-url}") private val kakaoApiBaseUrl: String,
    @Value("\${kakao.api.key}") private val kakaoApiKey: String
) {
    private val restTemplate: RestTemplate = RestTemplate()

    fun getAddressCoordinates(addr: String): Map<String, Double> {
        try {
            val headers = HttpHeaders()
            headers.set(HttpHeaders.AUTHORIZATION, "KakaoAK $kakaoApiKey")
            headers.accept = listOf(MediaType.APPLICATION_JSON)

            val uri = UriComponentsBuilder.fromUriString("$kakaoApiBaseUrl/v2/local/search/address.json")
                .queryParam("query", addr)
                .build()
                .toUriString()

            val requestEntity = org.springframework.http.HttpEntity(null, headers)

            val response = restTemplate.exchange(
                uri,
                HttpMethod.GET,
                requestEntity,
                KakaoResponse::class.java
            )

            val responseAddr = response.body?.documents?.firstOrNull()?.address
                ?: throw BaseException(ErrorCode.INVALID_ADDRESS)

            return mapOf("lat" to responseAddr.y.toDouble(), "lon" to responseAddr.x.toDouble())

        } catch (ex: Exception) {
            throw BaseException(ErrorCode.INVALID_ADDRESS)
        }

    }

    fun getDirections(
        startCoordinatesMap: Map<String, Double>,
        endCoordinatesMap: Map<String, Double>,
        ton: Double
    ) : Double{
        val headers = HttpHeaders()
        headers.set(HttpHeaders.AUTHORIZATION, "KakaoAK $kakaoApiKey")
        headers.accept = listOf(MediaType.APPLICATION_JSON)

        val uri = UriComponentsBuilder.fromUriString("https://apis-navi.kakaomobility.com/v1/directions")
            .queryParam("origin", "${startCoordinatesMap["lon"]},${startCoordinatesMap["lat"]}")
            .queryParam("destination", "${endCoordinatesMap["lon"]},${endCoordinatesMap["lat"]}")
            .queryParam("alternatives", false)
            .queryParam("priority", "RECOMMEND")
            .queryParam("road_details", false)
            .queryParam("car_type", Utils.getCarType(ton))
            .queryParam("car_hipass", true)
            .queryParam("summary", true)
            .build()
            .toUriString()

        println(uri)

        val requestEntity = org.springframework.http.HttpEntity(null, headers)

        val response = restTemplate.exchange(uri, HttpMethod.GET, requestEntity, String::class.java)

        // Convert the JSON response to a Map
        val objectMapper = ObjectMapper()
        val responseBody: Map<String, Any> = objectMapper.readValue(response.body, object : TypeReference<Map<String, Any>>() {})

        val routes = responseBody["routes"] as? List<*>
        val firstRoute = routes?.get(0)
        val summary = (firstRoute as Map<*, *>)["summary"]
        val distance = (summary as Map<*, *>)["distance"] as Int

        return convertToKilometers(distance)
    }

    private fun convertToKilometers(meters: Int): Double {
        val kilometers = meters / 1000.0
        return "%.1f".format(kilometers).toDouble()
    }


    fun calculateDistance(
        startCoordinatesMap: Map<String, Double>,
        endCoordinatesMap: Map<String, Double>,
        viaCoordinatesMap: Map<String, Double>?,
        isViaAddress: Boolean
    ): Double {
        val distance = if (isViaAddress) {
            DistanceUtil.getDistance(
                startCoordinatesMap["lat"]!!,
                startCoordinatesMap["lon"]!!,
                viaCoordinatesMap!!["lat"]!!,
                viaCoordinatesMap["lon"]!!
            ) + DistanceUtil.getDistance(
                viaCoordinatesMap["lat"]!!,
                viaCoordinatesMap["lon"]!!,
                endCoordinatesMap["lat"]!!,
                endCoordinatesMap["lon"]!!
            )
        } else {
            DistanceUtil.getDistance(
                startCoordinatesMap["lat"]!!,
                startCoordinatesMap["lon"]!!,
                endCoordinatesMap["lat"]!!,
                endCoordinatesMap["lon"]!!
            )
        }

        return distance
    }



//    // Function to calculate delivery fee based on transport distance and vehicle tonnage
//    fun calculateDeliveryFee(distance: Int, tonnage: Int): Int {
//        // 기본 요금 설정
//
//        // 기본 거리당 요금
//        val defaultDistanceRate = 1000 // 100 won
//        // 기본 톤당 요금
//        val defaultTonRate = 5000 // 5000 won
//
//        //추가 요금 설정
//
//        // 추가요금 거리 기준
//        val additionalDistanceThreshold = 30
//        //추가요금
//        val additionalFee = 200 // 200 won
//
//
//        var totalFee = distance * defaultDistanceRate // Default fee
//
//        // Apply additional fee
//        if (distance > additionalDistanceThreshold) {
//            val additionalDistance = distance - additionalDistanceThreshold
//            totalFee += additionalDistance * additionalFee
//        }
//
//        // Increase fee based on vehicle tonnage
//        val tonnageFee = tonnage * defaultTonRate
//        totalFee += tonnageFee
//
//        return totalFee
//    }



}
