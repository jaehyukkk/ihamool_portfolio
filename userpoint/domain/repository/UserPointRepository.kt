package com.ilogistic.delivery_admin_backend.userpoint.domain.repository

import com.ilogistic.delivery_admin_backend.user.domain.entity.User
import com.ilogistic.delivery_admin_backend.userpoint.domain.entity.UserPoint
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.transaction.annotation.Transactional

interface UserPointRepository : JpaRepository<UserPoint, Long>, UserPointRepositoryCustom{

    fun findTopByUserOrderByIdDesc(user : User) : UserPoint?
    fun findByUserOrderByIdDesc(user : User) : List<UserPoint>
}
