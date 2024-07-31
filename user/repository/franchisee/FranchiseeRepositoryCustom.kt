package com.ilogistic.delivery_admin_backend.user.repository.franchisee

import com.ilogistic.delivery_admin_backend.dto.common.PaginateDto
import com.ilogistic.delivery_admin_backend.user.domain.dto.FranchiseSearchResponseDto
import com.ilogistic.delivery_admin_backend.user.domain.dto.FranchiseeInfoResponseDto
import com.ilogistic.delivery_admin_backend.user.domain.dto.FranchiseeSearchDto
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable

interface FranchiseeRepositoryCustom {

    fun getFranchiseeList(franchiseeSearchDto: FranchiseeSearchDto, pageable: Pageable): Page<FranchiseSearchResponseDto>

    fun getFranchiseeInfo(franchiseeId: Long): FranchiseeInfoResponseDto
}
