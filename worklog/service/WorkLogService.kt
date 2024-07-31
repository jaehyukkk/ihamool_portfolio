package com.ilogistic.delivery_admin_backend.worklog.service

import com.ilogistic.delivery_admin_backend.entity.BaseEntity
import com.ilogistic.delivery_admin_backend.exception.BaseException
import com.ilogistic.delivery_admin_backend.exception.ErrorCode
import com.ilogistic.delivery_admin_backend.user.domain.entity.Driver
import com.ilogistic.delivery_admin_backend.worklog.domain.entity.WorkLog
import com.ilogistic.delivery_admin_backend.worklog.domain.repository.WorkLogRepository
import com.ilogistic.delivery_admin_backend.worklog.enums.CommuteType
import org.springframework.stereotype.Service

@Service
class WorkLogService(
    private val workLogRepository: WorkLogRepository
) {

    fun onWork(driver: Driver) {
        if(lastWorkType(driver) == CommuteType.ON){
            throw BaseException(ErrorCode.ALREADY_ON_WORK)
        }

        workLogRepository.save(
            WorkLog(
                driver = driver,
                type = CommuteType.ON
            )
        )
    }

    fun offWork(driver: Driver) {
        if(lastWorkType(driver) == CommuteType.OFF){
            throw BaseException(ErrorCode.ALREADY_OFF_WORK)
        }

        workLogRepository.save(
            WorkLog(
                driver = driver,
                type = CommuteType.OFF
            )
        )
    }

    fun lastWorkType(driver: Driver): CommuteType {
        return workLogRepository.getLastWorkType(driver)
    }
}
