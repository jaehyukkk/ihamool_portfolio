package com.ilogistic.delivery_admin_backend.cartype.domain.repository

import com.ilogistic.delivery_admin_backend.cartype.domain.entity.CarType
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional

@Repository
interface CarTypeRepository : JpaRepository<CarType, Long>, CarTypeRepositoryCustom{

    @Modifying
    @Transactional
    @Query("UPDATE CarType c SET c.name = :name, c.description = :description WHERE c.id = :id")
    fun modifyCarType(id: Long, name: String, description: String)

    fun findByParentIdOrderByIdDesc(parentId: Long): List<CarType>
}
