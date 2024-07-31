package com.ilogistic.delivery_admin_backend.dispatchcomplete.domain.dto

import com.ilogistic.delivery_admin_backend.annotation.ExcelColumn
import com.ilogistic.delivery_admin_backend.annotation.ExcelDataType
import com.querydsl.core.annotations.QueryProjection

data class DriverStatisticsResponseDto @QueryProjection constructor(
    @ExcelColumn("기사명", dataType = ExcelDataType.STRING)
    val name: String,
    @ExcelColumn("연락처", dataType = ExcelDataType.STRING)
    val phone: String,
    @ExcelColumn("차량번호", dataType = ExcelDataType.STRING)
    val carNumber: String,
    @ExcelColumn("총수익", dataType = ExcelDataType.NUMBER)
    val originalPrice: Int,
    @ExcelColumn("총순익", dataType = ExcelDataType.NUMBER)
    val driverPrice: Int,
    @ExcelColumn("선사비", dataType = ExcelDataType.NUMBER)
    val shipPrice: Int,
    @ExcelColumn("수수료", dataType = ExcelDataType.NUMBER)
    val feePrice: Int,
    @ExcelColumn("총운행횟수", dataType = ExcelDataType.NUMBER)
    val count: Long,
) {
}
