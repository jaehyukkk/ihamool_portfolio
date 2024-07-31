package com.ilogistic.delivery_admin_backend.dispatchlogging.domain.entity

import com.ilogistic.delivery_admin_backend.dispatch.enums.DispatchStatus
import com.ilogistic.delivery_admin_backend.dto.common.BaseEntity
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id

@Entity
class DispatchLogging(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id : Long? = null,
    var driverId: Long? = null,
    var dispatchId: Long,
    var status: DispatchStatus,
    var isForce: Boolean = false
): BaseEntity() {
}
