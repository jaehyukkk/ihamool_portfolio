package com.ilogistic.delivery_admin_backend.cartype.controller

import com.ilogistic.delivery_admin_backend.cartype.domain.dto.CarTypeRequestDto
import com.ilogistic.delivery_admin_backend.cartype.domain.dto.CarTypeResponseDto
import com.ilogistic.delivery_admin_backend.cartype.domain.dto.CarTypeSearchDto
import com.ilogistic.delivery_admin_backend.cartype.domain.dto.ParentListResponseDto
import com.ilogistic.delivery_admin_backend.cartype.domain.entity.CarType
import com.ilogistic.delivery_admin_backend.cartype.service.CarTypeService
import com.ilogistic.delivery_admin_backend.dto.common.PaginateDto
import org.springframework.data.domain.Page
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RequestMapping("/api/v1/car-type")
@RestController
class CarTypeController(
    private val carTypeService: CarTypeService
) {

    @PostMapping()
    fun create(@RequestBody carTypeRequestDto: CarTypeRequestDto): ResponseEntity<CarType> {
        return ResponseEntity.ok(carTypeService.create(carTypeRequestDto))
    }

    @GetMapping()
    fun list(paginateDto: PaginateDto, carTypeSearchDto: CarTypeSearchDto): ResponseEntity<Page<CarTypeResponseDto>> {
        return ResponseEntity.ok(carTypeService.list(paginateDto, carTypeSearchDto))
    }

    @GetMapping("/parent")
    fun parentList(): ResponseEntity<List<ParentListResponseDto>> {
        return ResponseEntity.ok(carTypeService.parentList())
    }


    @GetMapping("/{id}/children")
    fun children(@PathVariable id: Long): ResponseEntity<List<CarTypeResponseDto>> {

        return ResponseEntity.ok(carTypeService.childrenList(id))
    }


    @GetMapping("/{id}")
    fun detail(@PathVariable id: Long): ResponseEntity<CarType> {
        return ResponseEntity.ok(carTypeService.detail(id))
    }

    @PutMapping("/{id}")
    fun modify(@PathVariable id: Long, @RequestBody carTypeRequestDto: CarTypeRequestDto): ResponseEntity<Unit> {
        carTypeService.modify(id, carTypeRequestDto)
        return ResponseEntity.ok().build()
    }

    @DeleteMapping("/{ids}")
    fun delete(@PathVariable ids: List<Long>): ResponseEntity<Unit> {
        carTypeService.delete(ids)
        return ResponseEntity.ok().build()
    }

}
