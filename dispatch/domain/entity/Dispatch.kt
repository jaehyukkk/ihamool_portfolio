package com.ilogistic.delivery_admin_backend.dispatch.domain.entity

import com.fasterxml.jackson.annotation.JsonIgnore
import com.ilogistic.delivery_admin_backend.admingroup.domain.entity.AdminGroup
import com.ilogistic.delivery_admin_backend.cartype.domain.entity.CarType
import com.ilogistic.delivery_admin_backend.dispatch.domain.dto.DispatchRequestApproveRequestDto
import com.ilogistic.delivery_admin_backend.dispatch.enums.DispatchDateValue
import com.ilogistic.delivery_admin_backend.dispatch.enums.DispatchStatus
import com.ilogistic.delivery_admin_backend.dispatch.enums.LoadingMethod
import com.ilogistic.delivery_admin_backend.dispatch.enums.PaymentType
import com.ilogistic.delivery_admin_backend.dispatchcartype.domain.entity.DispatchCarType
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
@Table(name = "tb_dispatch")
@SQLDelete(sql = "UPDATE tb_dispatch SET deleted = true WHERE id = ?")
@Where(clause = "deleted = false")
class Dispatch(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,
    val startAddress: String,
    val startDetailAddress: String? = null,
    val startZipCode: String? = null,
    val startLatitude: Double,
    val startLongitude: Double,
    val startSigungu: String,
    var startSido: String,

    val endAddress: String,
    val endDetailAddress: String? = null,
    val endZipCode: String? = null,
    val endLatitude: Double,
    val endLongitude: Double,
    val endSigungu: String,
    var endSido: String,

    val viaAddress: String? = null,
    val viaDetailAddress: String? = null,
    val viaZipCode: String? = null,
    val viaLatitude: Double? = null,
    val viaLongitude: Double? = null,
    val viaSigungu: String? = null,
    val viaSido: String? = null,

    val distance: Double,
    val originalPrice: Int,
    var fee: Int,
    var driverPrice: Int,
    val loadingMethod: LoadingMethod,
    val itemName: String,
    val itemCount: Int,
    val palletCount: Int,
    val dispatchStart: DispatchDateValue,
    val dispatchEnd: DispatchDateValue,
    val addressType: String,
    val dispatchDateTime: LocalDateTime? = null,
    val loadingDateTime: LocalDateTime? = null,
    val completeDateTime: LocalDateTime? = null,

    var shipPrice: Int = 0,
    val paymentType: PaymentType,
    val dispatchCode: String,

    var status: DispatchStatus? = DispatchStatus.WAITING,

    @Column(columnDefinition = "TEXT")
    val precautions: String? = null,
    @Column(columnDefinition = "TEXT")
    val memo: String? = null,

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "driver_id")
    var driver: Driver? = null,

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "franchisee_id")
    var franchisee: Franchisee? = null,

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "final_approval_user_id")
    var finalApprovalUser: User? = null,

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    val user : User,

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_car_type_id")
    val parentCarType: CarType,

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "admin_group_id")
    val adminGroup: AdminGroup,

    val deleted: Boolean? = false,

    @OneToMany(mappedBy = "dispatch", fetch = FetchType.LAZY, cascade = [CascadeType.MERGE])
    val dispatchCarTypes: MutableSet<DispatchCarType> = LinkedHashSet()


) : BaseEntity() {

    fun dispatchApprove(dispatchRequestApproveRequestDto: DispatchRequestApproveRequestDto, driverPrice: Int, finalApprovalUser: User) : Dispatch {
        this.status = DispatchStatus.WAITING
        this.fee = dispatchRequestApproveRequestDto.fee
        this.shipPrice = dispatchRequestApproveRequestDto.shipPrice
        this.driverPrice = driverPrice
        this.finalApprovalUser = finalApprovalUser

        return this
    }
}
