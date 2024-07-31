package com.ilogistic.delivery_admin_backend.dispatchtemplate.domain.entity

import com.fasterxml.jackson.annotation.JsonIgnore
import com.ilogistic.delivery_admin_backend.dispatch.enums.DispatchDateValue
import com.ilogistic.delivery_admin_backend.dispatch.enums.DispatchStatus
import com.ilogistic.delivery_admin_backend.dispatch.enums.LoadingMethod
import com.ilogistic.delivery_admin_backend.dispatch.enums.PaymentType
import com.ilogistic.delivery_admin_backend.dispatchcartype.domain.entity.DispatchCarType
import com.ilogistic.delivery_admin_backend.dispatchtemplatecartype.domain.entity.DispatchTemplateCarType
import com.ilogistic.delivery_admin_backend.dto.common.BaseEntity
import com.ilogistic.delivery_admin_backend.user.domain.entity.Admin
import com.ilogistic.delivery_admin_backend.user.domain.entity.Driver
import com.ilogistic.delivery_admin_backend.user.domain.entity.Franchisee
import com.ilogistic.delivery_admin_backend.user.domain.entity.User
import org.hibernate.annotations.SQLDelete
import org.hibernate.annotations.Where
import java.time.LocalDateTime
import javax.persistence.*

@Entity
@SQLDelete(sql = "UPDATE dispatch_template SET deleted = true WHERE id = ?")
@Where(clause = "deleted = false")
class DispatchTemplate(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,
    val templateName: String,
    val startAddress: String? = null,
    val startDetailAddress: String? = null,
    val startZipCode: String? = null,
    val startSigungu: String? = null,
    var startSido: String? = null,

    val endAddress: String? = null,
    val endDetailAddress: String? = null,
    val endZipCode: String? = null,
    val endSigungu: String? = null,
    var endSido: String? = null,

    val originalPrice: Int? = null,
    val fee: Int? = null,
    val driverPrice: Int? = null,
    val loadingMethod: LoadingMethod? = null,
    val itemName: String? = null,
    val itemCount: Int? = null,
    val palletCount: Int? = null,
    val dispatchStart: DispatchDateValue? = null,
    val dispatchEnd: DispatchDateValue? = null,
    val addressType: String? = null,

    val shipPrice: Int? = null,
    val paymentType: PaymentType? = null,

    @OneToMany(mappedBy = "dispatchTemplate", fetch = FetchType.LAZY, cascade = [CascadeType.MERGE])
    val dispatchTemplateCarTypes: MutableSet<DispatchTemplateCarType> = LinkedHashSet(),

    @Column(columnDefinition = "TEXT")
    val precautions: String? = null,
    @Column(columnDefinition = "TEXT")
    val memo: String? = null,

    val deleted: Boolean? = false,

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "franchisee_id")
    var franchisee: Franchisee? = null,

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    val user: User


) : BaseEntity() {
}
