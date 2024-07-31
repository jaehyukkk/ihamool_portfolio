package com.ilogistic.delivery_admin_backend.userpoint.domain.repository

interface UserPointRepositoryCustom {

    fun getUserPoint(userId: Long) : Int?
}
