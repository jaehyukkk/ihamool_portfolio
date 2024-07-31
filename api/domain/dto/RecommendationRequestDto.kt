package com.ilogistic.delivery_admin_backend.api.domain.dto

class RecommendationRequestDto(
    val startAddress: String,
    val endAddress: String,
    val ton: Double,
) {
}
