package com.ilogistic.delivery_admin_backend.worklog.domain.repository

import com.ilogistic.delivery_admin_backend.worklog.domain.entity.WorkLog
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface WorkLogRepository : JpaRepository<WorkLog, Long>, WorkLogRepositoryCustom {
}
