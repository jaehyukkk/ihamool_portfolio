package com.ilogistic.delivery_admin_backend.dispatchtemplatecartype.service

import com.ilogistic.delivery_admin_backend.cartype.service.CarTypeService
import com.ilogistic.delivery_admin_backend.dispatch.domain.entity.Dispatch
import com.ilogistic.delivery_admin_backend.dispatchcartype.domain.entity.DispatchCarType
import com.ilogistic.delivery_admin_backend.dispatchtemplate.domain.entity.DispatchTemplate
import com.ilogistic.delivery_admin_backend.dispatchtemplatecartype.domain.entity.DispatchTemplateCarType
import com.ilogistic.delivery_admin_backend.dispatchtemplatecartype.domain.repository.DispatchTemplateCarTypeRepository
import org.springframework.stereotype.Service

@Service
class DispatchTemplateCarTypeService(
    private val dispatchTemplateCarTypeRepository: DispatchTemplateCarTypeRepository,
    private val carTypeService: CarTypeService
) {

    fun create(dispatchTemplate: DispatchTemplate, carTypeId: Long) {
        dispatchTemplateCarTypeRepository.save(
            DispatchTemplateCarType(
                dispatchTemplate = dispatchTemplate,
                carType = carTypeService.detail(carTypeId)
            )
        )
    }
}
