package com.ilogistic.delivery_admin_backend.dispatchtemplate.domain.repository

import com.ilogistic.delivery_admin_backend.dispatchtemplate.domain.dto.DispatchTemplateResponseDto
import com.ilogistic.delivery_admin_backend.dispatchtemplate.domain.dto.DispatchTemplateSearchDto
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable

interface DispatchTemplateRepositoryCustom {

    fun getDispatchTemplateList(pageable: Pageable, userId : Long, dispatchTemplateSearchDto: DispatchTemplateSearchDto) : Page<DispatchTemplateResponseDto>

    fun getDispatchTemplate(id: Long) : DispatchTemplateResponseDto
}
