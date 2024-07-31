package com.ilogistic.delivery_admin_backend.user.repository.callworker

import com.ilogistic.delivery_admin_backend.user.domain.entity.CallWorker
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface CallWorkerRepository : JpaRepository<CallWorker, Long> {
}
