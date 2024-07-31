package com.ilogistic.delivery_admin_backend.admingroup.domain.repository

import com.ilogistic.delivery_admin_backend.admingroup.domain.dto.AdminGroupResponseDto
import com.ilogistic.delivery_admin_backend.admingroup.domain.dto.QAdminGroupResponseDto
import com.ilogistic.delivery_admin_backend.admingroup.domain.entity.QAdminGroup.adminGroup
import com.querydsl.jpa.impl.JPAQueryFactory
import org.springframework.stereotype.Repository

@Repository
class AdminGroupRepositoryImpl(
    private val queryFactory: JPAQueryFactory
) : AdminGroupRepositoryCustom{

    override fun getAdminGroupList(): List<AdminGroupResponseDto> {
        return queryFactory.select(
            QAdminGroupResponseDto(
                adminGroup.id,
                adminGroup.companyName,
                adminGroup.companyNumber,
                adminGroup.ceoName,
                adminGroup.ceoPhone,
                adminGroup.address,
                adminGroup.detailAddress,
                adminGroup.zipCode,
                adminGroup.barobillId
            )
        ).from(adminGroup)
            .orderBy(adminGroup.id.desc())
            .fetch()
    }

    override fun getAdminGroupDetail(id: Long): AdminGroupResponseDto? {
        return queryFactory.select(
            QAdminGroupResponseDto(
                adminGroup.id,
                adminGroup.companyName,
                adminGroup.companyNumber,
                adminGroup.ceoName,
                adminGroup.ceoPhone,
                adminGroup.address,
                adminGroup.detailAddress,
                adminGroup.zipCode,
                adminGroup.barobillId
            )
        ).from(adminGroup)
            .where(adminGroup.id.eq(id))
            .fetchOne()
    }
}
