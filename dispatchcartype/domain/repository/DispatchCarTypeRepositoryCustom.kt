package com.ilogistic.delivery_admin_backend.dispatchcartype.domain.repository

interface DispatchCarTypeRepositoryCustom {

    fun getDispatchCarTypeList(dispatchId: Long): List<Long>
}
