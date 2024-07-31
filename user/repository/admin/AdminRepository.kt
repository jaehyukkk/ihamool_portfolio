package com.ilogistic.delivery_admin_backend.user.repository.admin

import com.ilogistic.delivery_admin_backend.user.domain.entity.Admin
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface AdminRepository : JpaRepository<Admin, Long> {
}
