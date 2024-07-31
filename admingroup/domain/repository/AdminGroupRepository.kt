package com.ilogistic.delivery_admin_backend.admingroup.domain.repository

import com.ilogistic.delivery_admin_backend.admingroup.domain.dto.AdminGroupRequestDto
import com.ilogistic.delivery_admin_backend.admingroup.domain.entity.AdminGroup
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional

@Repository
interface AdminGroupRepository : JpaRepository<AdminGroup, Long>, AdminGroupRepositoryCustom{

    fun existsByCompanyNumber(companyNumber: String): Boolean
    @Transactional
    @Modifying
    @Query("""
        UPDATE AdminGroup ag
        SET ag.companyName = :#{#adminGroupRequestDto.companyName}
        , ag.companyNumber = :#{#adminGroupRequestDto.companyNumber}
        , ag.ceoName = :#{#adminGroupRequestDto.ceoName}
        , ag.ceoPhone = :#{#adminGroupRequestDto.ceoPhone}
        , ag.address = :#{#adminGroupRequestDto.address}
        , ag.detailAddress = :#{#adminGroupRequestDto.detailAddress}
        , ag.zipCode = :#{#adminGroupRequestDto.zipCode}
        , ag.barobillId = :#{#adminGroupRequestDto.barobillId}
        WHERE ag.id = :id
    """)
    fun modify(id: Long, adminGroupRequestDto: AdminGroupRequestDto) : Int
}
