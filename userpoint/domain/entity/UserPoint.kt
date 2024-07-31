package com.ilogistic.delivery_admin_backend.userpoint.domain.entity

import com.fasterxml.jackson.annotation.JsonIgnore
import com.ilogistic.delivery_admin_backend.dto.common.BaseEntity
import com.ilogistic.delivery_admin_backend.user.domain.entity.Driver
import com.ilogistic.delivery_admin_backend.user.domain.entity.Franchisee
import com.ilogistic.delivery_admin_backend.user.domain.entity.User
import com.ilogistic.delivery_admin_backend.userpoint.enums.PointReason
import com.ilogistic.delivery_admin_backend.userpoint.enums.PointType
import javax.persistence.*

@Entity
class UserPoint(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    var user: User? = null,

//    @JsonIgnore
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "handler_user_id")
//    var handlerUser: User? = null,

    //현재 포인트
    var currentPoint: Int = 0,
    //지급
    var chargedPoint: Int = 0,
    //차감
    var deductedPoint: Int = 0,

    var pointReason: PointReason,

    var pointType: PointType,

    val memo: String? = null,
): BaseEntity() {
}
