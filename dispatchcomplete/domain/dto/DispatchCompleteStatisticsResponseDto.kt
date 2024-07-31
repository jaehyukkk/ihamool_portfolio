package com.ilogistic.delivery_admin_backend.dispatchcomplete.domain.dto

import com.querydsl.core.annotations.QueryProjection

data class DispatchCompleteStatisticsResponseDto @QueryProjection constructor(
    //완료건수
    var completeCount: Long,
    //총금액
    var totalAmount: Int,
    //수수료 제외 금액
    var totalAmountWithoutFee: Int,
    //총 거리
    var totalDistance: Double,
) {
}
