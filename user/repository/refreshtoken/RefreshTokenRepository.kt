package com.ilogistic.delivery_admin_backend.user.repository.refreshtoken

import com.ilogistic.delivery_admin_backend.user.domain.entity.RefreshToken
import org.springframework.data.jpa.repository.JpaRepository

interface RefreshTokenRepository: JpaRepository<RefreshToken, Long> {
    fun findByKey(key: Long): RefreshToken?
}
