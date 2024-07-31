package com.ilogistic.delivery_admin_backend.admingroup.domain.repository

import com.ilogistic.delivery_admin_backend.admingroup.domain.dto.AdminGroupResponseDto
import org.springframework.data.domain.Page

interface AdminGroupRepositoryCustom{
    fun getAdminGroupList(): List<AdminGroupResponseDto>

    fun getAdminGroupDetail(id: Long): AdminGroupResponseDto?
}
