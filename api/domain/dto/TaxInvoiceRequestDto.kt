package com.ilogistic.delivery_admin_backend.api.domain.dto

import com.querydsl.core.annotations.QueryProjection

data class TaxInvoiceRequestDto @QueryProjection constructor(
    val dispatchId: Long,
    val amount: Int,
    val driverCompanyName: String,
    val driverCompanyNumber: String,
    val driverName: String,
    val driverAddress: String,
    val driverDetailAddress: String,

    val adminGroupCompanyName: String,
    val adminGroupCompanyNumber: String,
    val adminGroupCeoName: String,
    val adminGroupAddress: String,
    val adminGroupDetailAddress: String,

    val barobillId: String? = null,
) {
}
