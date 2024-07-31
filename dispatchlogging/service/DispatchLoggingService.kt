package com.ilogistic.delivery_admin_backend.dispatchlogging.service

import com.ilogistic.delivery_admin_backend.dispatch.enums.DispatchStatus
import com.ilogistic.delivery_admin_backend.dispatchlogging.domain.entity.DispatchLogging
import com.ilogistic.delivery_admin_backend.dispatchlogging.domain.repository.DispatchLoggingRepository
import org.springframework.stereotype.Service

@Service
class DispatchLoggingService(
    private val dispatchLoggingRepository: DispatchLoggingRepository
) {
    fun logging(driverId: Long?, dispatchId: Long, status: DispatchStatus, isForce: Boolean = false) {
        dispatchLoggingRepository.save(DispatchLogging(
            driverId = driverId,
            dispatchId = dispatchId,
            status = status,
            isForce = isForce
        ))
    }

    fun getDriverCancelCount(driverId: Long, dispatchId: Long, status: DispatchStatus): Long? {
        return dispatchLoggingRepository.getDriverCancelCount(driverId, dispatchId, status)
    }

    fun getLoggingList(dispatchId: Long) = dispatchLoggingRepository.getLoggingList(dispatchId)
}
