package com.ilogistic.delivery_admin_backend.dispatchcartype.domain.entity

import com.fasterxml.jackson.annotation.JsonIgnore
import com.ilogistic.delivery_admin_backend.cartype.domain.entity.CarType
import com.ilogistic.delivery_admin_backend.dispatch.domain.entity.Dispatch
import javax.persistence.*

@Entity
class DispatchCarType(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "dispatch_id")
    val dispatch: Dispatch,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "car_type_id")
    val carType: CarType

) {
}
