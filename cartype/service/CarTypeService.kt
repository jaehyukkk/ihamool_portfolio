package com.ilogistic.delivery_admin_backend.cartype.service

import com.ilogistic.delivery_admin_backend.cartype.domain.dto.CarTypeRequestDto
import com.ilogistic.delivery_admin_backend.cartype.domain.dto.CarTypeResponseDto
import com.ilogistic.delivery_admin_backend.cartype.domain.dto.CarTypeSearchDto
import com.ilogistic.delivery_admin_backend.cartype.domain.dto.ParentListResponseDto
import com.ilogistic.delivery_admin_backend.cartype.domain.entity.CarType
import com.ilogistic.delivery_admin_backend.cartype.domain.repository.CarTypeRepository
import com.ilogistic.delivery_admin_backend.dto.common.PaginateDto
import org.springframework.data.domain.Page
import org.springframework.stereotype.Service

@Service
class CarTypeService(
    private val carTypeRepository: CarTypeRepository
) {

    fun create(carTypeRequestDto: CarTypeRequestDto) : CarType {
        return carTypeRepository.save(carTypeRequestDto.toEntity())
    }

    fun list(paginateDto: PaginateDto, carTypeSearchDto: CarTypeSearchDto) : Page<CarTypeResponseDto> {
        return carTypeRepository.getCarTypeList(paginateDto.pageable(), carTypeSearchDto)
    }

    fun parentList() : List<ParentListResponseDto> {
        return carTypeRepository.getParentList()
    }

    fun childrenList(id: Long) : List<CarTypeResponseDto> {
        return carTypeRepository.getChildrenList(id)
    }

    fun modify(id: Long, carTypeRequestDto: CarTypeRequestDto) {
        carTypeRepository.modifyCarType(id, carTypeRequestDto.name, carTypeRequestDto.description)
    }

    fun delete(ids: List<Long>) {
        ids.forEach { id ->
            carTypeRepository.deleteById(id)
        }
    }

    fun detail(id: Long) : CarType {
        return carTypeRepository.findById(id).orElseThrow { IllegalArgumentException("해당 차종이 존재하지 않습니다.") }
    }

}
