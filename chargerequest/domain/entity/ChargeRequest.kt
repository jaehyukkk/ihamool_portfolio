package com.ilogistic.delivery_admin_backend.chargerequest.domain.entity

import com.ilogistic.delivery_admin_backend.chargerequest.enums.ChargeRequestStatus
import com.ilogistic.delivery_admin_backend.entity.BaseEntity
import com.ilogistic.delivery_admin_backend.user.domain.entity.User
import com.ilogistic.delivery_admin_backend.userpoint.domain.entity.UserPoint
import javax.persistence.*

@Entity
class ChargeRequest(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,
    val point: Int,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    var user: User,

    val status: ChargeRequestStatus = ChargeRequestStatus.REQUESTED,

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_point_id")
    var userPoint: UserPoint? = null
) : BaseEntity(){
}
