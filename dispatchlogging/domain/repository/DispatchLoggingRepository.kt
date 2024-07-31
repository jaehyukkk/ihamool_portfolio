package com.ilogistic.delivery_admin_backend.dispatchlogging.domain.repository

import com.ilogistic.delivery_admin_backend.dispatchlogging.domain.entity.DispatchLogging
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface DispatchLoggingRepository : JpaRepository<DispatchLogging, Long>, DispatchLoggingRepositoryCustom {
}
