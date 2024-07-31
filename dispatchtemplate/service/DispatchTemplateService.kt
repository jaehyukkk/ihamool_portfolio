package com.ilogistic.delivery_admin_backend.dispatchtemplate.service

import com.ilogistic.delivery_admin_backend.dispatchtemplate.domain.dto.DispatchTemplateRequestDto
import com.ilogistic.delivery_admin_backend.dispatchtemplate.domain.dto.DispatchTemplateResponseDto
import com.ilogistic.delivery_admin_backend.dispatchtemplate.domain.dto.DispatchTemplateSearchDto
import com.ilogistic.delivery_admin_backend.dispatchtemplate.domain.entity.DispatchTemplate
import com.ilogistic.delivery_admin_backend.dispatchtemplate.domain.repository.DispatchTemplateRepository
import com.ilogistic.delivery_admin_backend.dispatchtemplatecartype.service.DispatchTemplateCarTypeService
import com.ilogistic.delivery_admin_backend.dto.common.PaginateDto
import com.ilogistic.delivery_admin_backend.user.service.UserService
import org.springframework.data.domain.Page
import org.springframework.stereotype.Service

@Service
class DispatchTemplateService(
    private val dispatchTemplateRepository: DispatchTemplateRepository,
    private val userService: UserService,
    private val dispatchTemplateCarTypeService: DispatchTemplateCarTypeService
) {

    fun create(dispatchTemplateRequestDto: DispatchTemplateRequestDto, userId : Long){
        if(dispatchTemplateRequestDto.franchiseeId != null){
            dispatchTemplateRequestDto.franchisee = userService.getFranchiseeEntity(dispatchTemplateRequestDto.franchiseeId)
        }
        val user = userService.getUser(userId)
        val dispatchTemplate =  dispatchTemplateRepository.save(dispatchTemplateRequestDto.toEntity(user))
        dispatchTemplateRequestDto.carTypeIds?.forEach { carTypeId ->
            dispatchTemplateCarTypeService.create(dispatchTemplate, carTypeId)
        }
    }

    fun list(paginateDto: PaginateDto, userId : Long, dispatchTemplateSearchDto: DispatchTemplateSearchDto) : Page<DispatchTemplateResponseDto>{
        return dispatchTemplateRepository.getDispatchTemplateList(paginateDto.pageable(), userId, dispatchTemplateSearchDto)
    }

    fun getDispatchTemplate(id: Long) : DispatchTemplateResponseDto{
        return dispatchTemplateRepository.getDispatchTemplate(id)
    }
}
