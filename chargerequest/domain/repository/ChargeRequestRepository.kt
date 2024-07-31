package com.ilogistic.delivery_admin_backend.chargerequest.domain.repository

import com.ilogistic.delivery_admin_backend.chargerequest.domain.entity.ChargeRequest
import com.ilogistic.delivery_admin_backend.chargerequest.enums.ChargeRequestStatus
import com.ilogistic.delivery_admin_backend.userpoint.domain.entity.UserPoint
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import javax.transaction.Transactional

@Repository
interface ChargeRequestRepository : JpaRepository<ChargeRequest, Long> , ChargeRequestRepositoryCustom{

    @Transactional
    @Modifying
    @Query("update ChargeRequest cr set cr.status = :chargeRequestStatus, cr.userPoint = :userPoint where cr.id = :chargeRequestId")
    fun approve(chargeRequestId: Long, chargeRequestStatus: ChargeRequestStatus, userPoint: UserPoint) : Int

}
