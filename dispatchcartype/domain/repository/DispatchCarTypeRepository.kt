package com.ilogistic.delivery_admin_backend.dispatchcartype.domain.repository

import com.ilogistic.delivery_admin_backend.dispatchcartype.domain.entity.DispatchCarType
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface DispatchCarTypeRepository : JpaRepository<DispatchCarType, Long>, DispatchCarTypeRepositoryCustom{
}
