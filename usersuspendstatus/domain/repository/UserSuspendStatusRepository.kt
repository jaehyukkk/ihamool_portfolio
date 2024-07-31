package com.ilogistic.delivery_admin_backend.usersuspendstatus.domain.repository

import com.ilogistic.delivery_admin_backend.user.domain.entity.User
import com.ilogistic.delivery_admin_backend.usersuspendstatus.domain.entity.UserSuspendStatus
import com.ilogistic.delivery_admin_backend.usersuspendstatus.enums.SuspendType
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional

@Repository
interface UserSuspendStatusRepository : JpaRepository<UserSuspendStatus, Long>, UserSuspendStatusRepositoryCustom {

    fun existsByUserAndStatusAndSuspendTypeAndReason(user: User, status: Boolean, suspendType: SuspendType, reason: String): Boolean


    @Modifying
    @Transactional
    @Query("delete from UserSuspendStatus u where u.user = :user and u.suspendType = :suspendType")
    fun deleteSuspensionHistoryByUnpaidFee(user: User, suspendType: SuspendType)


}
