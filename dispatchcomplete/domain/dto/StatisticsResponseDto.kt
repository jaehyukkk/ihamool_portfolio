package com.ilogistic.delivery_admin_backend.dispatchcomplete.domain.dto

import com.ilogistic.delivery_admin_backend.annotation.ExcelColumn
import com.ilogistic.delivery_admin_backend.annotation.ExcelDataType
import com.querydsl.core.annotations.QueryProjection

data class StatisticsResponseDto @QueryProjection constructor(
    @ExcelColumn("총건수", dataType = ExcelDataType.NUMBER)
    val count: Long,
    @ExcelColumn("총매출", dataType = ExcelDataType.NUMBER)
    val totalAmount: Int,
    @ExcelColumn("순익", dataType = ExcelDataType.NUMBER)
    val totalAmountWithoutFee: Int,
    @ExcelColumn("일자", dataType = ExcelDataType.DATE )
    val date: String,
) {
}
