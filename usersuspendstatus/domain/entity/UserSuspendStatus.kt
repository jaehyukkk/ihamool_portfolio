package com.ilogistic.delivery_admin_backend.usersuspendstatus.domain.entity

import com.fasterxml.jackson.annotation.JsonIgnore
import com.ilogistic.delivery_admin_backend.user.domain.entity.User
import com.ilogistic.delivery_admin_backend.usersuspendstatus.enums.SuspendType
import org.hibernate.annotations.SQLDelete
import org.hibernate.annotations.Where
import javax.persistence.*

@Entity
@SQLDelete(sql = "UPDATE user_suspend_status SET deleted = true WHERE id = ?")
@Where(clause = "deleted = false")
class UserSuspendStatus(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    var user : User,

    var suspendType: SuspendType,
    var status: Boolean? = true,
    var reason: String,
) {

    val deleted = false
}
