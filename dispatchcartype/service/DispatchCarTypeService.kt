package com.ilogistic.delivery_admin_backend.dispatchcartype.service

import com.ilogistic.delivery_admin_backend.cartype.service.CarTypeService
import com.ilogistic.delivery_admin_backend.dispatch.domain.entity.Dispatch
import com.ilogistic.delivery_admin_backend.dispatchcartype.domain.entity.DispatchCarType
import com.ilogistic.delivery_admin_backend.dispatchcartype.domain.repository.DispatchCarTypeRepository
import org.springframework.stereotype.Service

@Service
class DispatchCarTypeService(
    private val dispatchCarTypeRepository: DispatchCarTypeRepository,
    private val carTypeService: CarTypeService
) {

    fun create(dispatch: Dispatch, carTypeIds: List<Long>) {
        carTypeIds.forEach { carTypeId ->
            dispatchCarTypeRepository.save(DispatchCarType(
                dispatch = dispatch,
                carType = carTypeService.detail(carTypeId)
            ))
        }
    }

    fun getDispatchCarTypeList(dispatchId: Long): List<Long> {
        return dispatchCarTypeRepository.getDispatchCarTypeList(dispatchId)
    }
}
