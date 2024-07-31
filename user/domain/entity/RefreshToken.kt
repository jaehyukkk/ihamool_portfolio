package com.ilogistic.delivery_admin_backend.user.domain.entity

import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Id

@Entity
class RefreshToken(
        @Id
        @Column(name = "rt_key")
        var key: Long,
        var token: String,
) {

    fun updateValue(token: String): RefreshToken {
        this.token = token
        return this
    }
}
