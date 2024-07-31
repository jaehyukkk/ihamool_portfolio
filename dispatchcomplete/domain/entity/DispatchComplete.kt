package com.ilogistic.delivery_admin_backend.dispatchcomplete.domain.entity

import com.fasterxml.jackson.annotation.JsonIgnore
import com.ilogistic.delivery_admin_backend.dispatch.domain.entity.Dispatch
import com.ilogistic.delivery_admin_backend.entity.BaseEntity
import com.ilogistic.delivery_admin_backend.user.domain.entity.Driver
import java.time.LocalDateTime
import javax.persistence.*

@Entity
class DispatchComplete (
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,

    @JsonIgnore
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "dispatch_id")
    var dispatch: Dispatch,

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "driver_id")
    var driver: Driver,

    val isTaxInvoice: Boolean = false,

    val taxInvoiceIssueDate: LocalDateTime? = null

    ) : BaseEntity(){
}
