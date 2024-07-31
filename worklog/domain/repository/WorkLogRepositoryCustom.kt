package com.ilogistic.delivery_admin_backend.worklog.domain.repository

import com.ilogistic.delivery_admin_backend.user.domain.entity.Driver
import com.ilogistic.delivery_admin_backend.worklog.enums.CommuteType

interface WorkLogRepositoryCustom {

    fun getLastWorkType(driver: Driver): CommuteType
}
