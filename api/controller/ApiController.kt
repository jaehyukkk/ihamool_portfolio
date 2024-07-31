package com.ilogistic.delivery_admin_backend.api.controller

import com.ilogistic.delivery_admin_backend.aop.UserRights
import com.ilogistic.delivery_admin_backend.api.domain.dto.AddressRequestDto
import com.ilogistic.delivery_admin_backend.api.domain.dto.RecommendationRequestDto
import com.ilogistic.delivery_admin_backend.api.service.ApiService
import com.ilogistic.delivery_admin_backend.api.service.BarobillService
import com.ilogistic.delivery_admin_backend.utils.DistanceUtil
import com.ilogistic.delivery_admin_backend.utils.Utils
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RestController
import java.security.Principal

@RestController
class ApiController(
    private val apiService: ApiService,
    private val barobillService: BarobillService
) {

    @UserRights
    @GetMapping("/api/v1/distance")
    fun getDistance(addressRequestDto: AddressRequestDto) : ResponseEntity<Double>{
        val isViaAddress = addressRequestDto.viaAddress.isNullOrBlank().not()
        println("isViaAddress: $isViaAddress")
        val startCoordinatesMap = apiService.getAddressCoordinates(addressRequestDto.startAddress)
        val endCoordinatesMap = apiService.getAddressCoordinates(addressRequestDto.endAddress)
        val viaCoordinatesMap = if (isViaAddress) apiService.getAddressCoordinates(addressRequestDto.viaAddress!!) else null
        return ResponseEntity.ok(apiService.calculateDistance(startCoordinatesMap, endCoordinatesMap, viaCoordinatesMap, isViaAddress))
    }


    @UserRights
    //추천요금 api
    @GetMapping("/api/v1/recommendation")
    fun getRecommendation(recommendationRequestDto: RecommendationRequestDto) : ResponseEntity<Int>{
        val startCoordinatesMap = apiService.getAddressCoordinates(recommendationRequestDto.startAddress)
        val endCoordinatesMap = apiService.getAddressCoordinates(recommendationRequestDto.endAddress)
        val distance = apiService.getDirections(startCoordinatesMap, endCoordinatesMap, recommendationRequestDto.ton)

        return ResponseEntity.ok(Utils.calculateDeliveryFee(distance.toInt(), recommendationRequestDto.ton))
    }

    @UserRights
    @PostMapping("/api/v1/taxinvoice/{dispatchId}")
    fun getTaxinvoice(@PathVariable dispatchId: Long, principal: Principal) : ResponseEntity<String>{
        barobillService.registAndIssueBrokerTaxInvoice(dispatchId, principal.name.toLong())
        return ResponseEntity.ok("success")
    }


}
