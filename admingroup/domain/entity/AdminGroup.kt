package com.ilogistic.delivery_admin_backend.admingroup.domain.entity

import com.ilogistic.delivery_admin_backend.entity.BaseEntity
import org.hibernate.annotations.SQLDelete
import org.hibernate.annotations.Where
import javax.persistence.*

@Entity
@SQLDelete(sql = "UPDATE admin_group SET deleted = true WHERE id = ?")
@Where(clause = "deleted = false")
class AdminGroup(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,
    val companyName: String,
    val companyNumber: String,
    val ceoName: String,
    val ceoPhone: String,
    val address: String,
    val detailAddress: String,
    val zipCode: String,
    val barobillId: String? = null,

    @Column(unique = true, nullable = false)
    val groupCode: String,

    val deleted: Boolean = false
): BaseEntity() {
}
