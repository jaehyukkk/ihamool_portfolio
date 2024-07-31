package com.ilogistic.delivery_admin_backend.applicationfeepayment.domain.entity

import com.fasterxml.jackson.annotation.JsonIgnore
import com.ilogistic.delivery_admin_backend.applicationfeepayment.enums.PaymentStatus
import com.ilogistic.delivery_admin_backend.entity.BaseEntity
import com.ilogistic.delivery_admin_backend.user.domain.entity.User
import com.ilogistic.delivery_admin_backend.userpoint.domain.entity.UserPoint
import javax.persistence.*

@Entity
class ApplicationFeePayment(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @JsonIgnore
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_point_id")
    var userPoint: UserPoint? = null,

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    var user: User,

    var status: PaymentStatus,

    ) : BaseEntity(){
}
