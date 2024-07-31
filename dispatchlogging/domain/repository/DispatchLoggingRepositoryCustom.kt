package com.ilogistic.delivery_admin_backend.dispatchlogging.domain.repository

import com.ilogistic.delivery_admin_backend.dispatch.enums.DispatchStatus
import com.ilogistic.delivery_admin_backend.dispatchlogging.domain.dto.DispatchLoggingResponseDto

interface DispatchLoggingRepositoryCustom {

    fun getDriverCancelCount(driverId: Long, dispatchId: Long, status: DispatchStatus): Long?

    fun getLoggingList(dispatchId: Long): List<DispatchLoggingResponseDto>

}
