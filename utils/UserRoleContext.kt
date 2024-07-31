package com.ilogistic.delivery_admin_backend.utils

import org.springframework.stereotype.Component

@Component
object UserRoleContext {
    private val userRoleThreadLocal = ThreadLocal<String>()
    var userRole: String
        get() = userRoleThreadLocal.get()
        set(role) {
            userRoleThreadLocal.set(role)
        }

    fun clear() {
        userRoleThreadLocal.remove()
    }
}
