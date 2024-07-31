package com.ilogistic.delivery_admin_backend.dispatchtemplate.domain.repository

import com.ilogistic.delivery_admin_backend.dispatchtemplate.domain.entity.DispatchTemplate
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface DispatchTemplateRepository : JpaRepository<DispatchTemplate, Long>, DispatchTemplateRepositoryCustom{
}
