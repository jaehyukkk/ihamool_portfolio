package com.ilogistic.delivery_admin_backend.cartype.domain.entity

import com.ilogistic.delivery_admin_backend.entity.BaseEntity
import com.ilogistic.delivery_admin_backend.user.domain.entity.Driver
import com.ilogistic.delivery_admin_backend.user.domain.entity.Franchisee
import javax.persistence.*

@Entity
class CarType(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,
    val name: String,
    val description: String,
    val parentId: Long? = null,
    val ton: Double? = null,

) : BaseEntity(){
}
