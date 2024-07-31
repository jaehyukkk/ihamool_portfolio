package com.ilogistic.delivery_admin_backend.cartype.domain.dto

import com.ilogistic.delivery_admin_backend.cartype.domain.entity.CarType

class CarTypeRequestDto(
    val name: String,
    val description: String,
    val parentId: Long? = null,
    val ton: Double? = null
) {

    fun toEntity() : CarType{
        return CarType(
            name = name,
            description = description,
            parentId = parentId,
            ton = ton
        )
    }
}
