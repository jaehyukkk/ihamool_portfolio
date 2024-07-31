package com.ilogistic.delivery_admin_backend.user.domain.entity

import com.ilogistic.delivery_admin_backend.entity.BaseEntity
import javax.persistence.Entity
import javax.persistence.Id

@Entity
class CallWorker(
    @Id
    val id: Long? = null,
    val name: String,
    val phone: String,
    val number: Int,
) : BaseEntity(){
}
