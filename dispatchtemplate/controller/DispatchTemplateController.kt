package com.ilogistic.delivery_admin_backend.dispatchtemplate.controller

import com.ilogistic.delivery_admin_backend.dispatchtemplate.domain.dto.DispatchTemplateRequestDto
import com.ilogistic.delivery_admin_backend.dispatchtemplate.domain.dto.DispatchTemplateResponseDto
import com.ilogistic.delivery_admin_backend.dispatchtemplate.domain.dto.DispatchTemplateSearchDto
import com.ilogistic.delivery_admin_backend.dispatchtemplate.domain.entity.DispatchTemplate
import com.ilogistic.delivery_admin_backend.dispatchtemplate.service.DispatchTemplateService
import com.ilogistic.delivery_admin_backend.dto.common.PaginateDto
import org.springframework.data.domain.Page
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.security.Principal

@RequestMapping("/api/v1/dispatch-template")
@RestController
class DispatchTemplateController(
    private val dispatchTemplateService: DispatchTemplateService
) {

    @PostMapping()
    fun create(@RequestBody dispatchTemplateRequestDto : DispatchTemplateRequestDto, principal: Principal) : ResponseEntity<Void>{
        dispatchTemplateService.create(dispatchTemplateRequestDto, principal.name.toLong())
        return ResponseEntity.ok().build()
    }

    @GetMapping()
    fun list(paginateDto: PaginateDto, principal: Principal, dispatchTemplateSearchDto: DispatchTemplateSearchDto): ResponseEntity<Page<DispatchTemplateResponseDto>> {
        return ResponseEntity.ok(dispatchTemplateService.list(paginateDto, principal.name.toLong(), dispatchTemplateSearchDto))
    }

    @GetMapping("/{id}")
    fun getDispatchTemplate(@PathVariable id: Long): ResponseEntity<DispatchTemplateResponseDto> {
        return ResponseEntity.ok(dispatchTemplateService.getDispatchTemplate(id))
    }
}
