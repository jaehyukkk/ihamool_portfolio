package com.ilogistic.delivery_admin_backend.user.repository.franchisee

import com.ilogistic.delivery_admin_backend.user.domain.entity.Franchisee
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface FranchiseeRepository : JpaRepository<Franchisee, Long>, FranchiseeRepositoryCustom{
}
