package com.ilogistic.delivery_admin_backend.cartype.domain.repository

import com.ilogistic.delivery_admin_backend.cartype.domain.dto.CarTypeResponseDto
import com.ilogistic.delivery_admin_backend.cartype.domain.dto.CarTypeSearchDto
import com.ilogistic.delivery_admin_backend.cartype.domain.dto.ParentListResponseDto
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable

interface CarTypeRepositoryCustom {
    fun getCarTypeList(pageable: Pageable, carTypeSearchDto: CarTypeSearchDto): Page<CarTypeResponseDto>

    fun getChildrenList(id: Long): List<CarTypeResponseDto>

    fun getParentList(): List<ParentListResponseDto>
}
