package com.ilogistic.delivery_admin_backend.dispatchtemplatecartype.domain.entity

import com.ilogistic.delivery_admin_backend.cartype.domain.entity.CarType
import com.ilogistic.delivery_admin_backend.dispatch.domain.entity.Dispatch
import com.ilogistic.delivery_admin_backend.dispatchtemplate.domain.entity.DispatchTemplate
import javax.persistence.*

@Entity
class DispatchTemplateCarType(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "dispatch_template_id")
    val dispatchTemplate: DispatchTemplate,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "car_type_id")
    val carType: CarType

) {
}

