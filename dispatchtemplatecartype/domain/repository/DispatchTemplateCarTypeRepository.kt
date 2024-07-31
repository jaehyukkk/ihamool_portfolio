package com.ilogistic.delivery_admin_backend.dispatchtemplatecartype.domain.repository

import com.ilogistic.delivery_admin_backend.dispatchtemplatecartype.domain.entity.DispatchTemplateCarType
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface DispatchTemplateCarTypeRepository : JpaRepository<DispatchTemplateCarType, Long>{
}
