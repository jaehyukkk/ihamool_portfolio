package com.ilogistic.delivery_admin_backend.aop

import com.ilogistic.delivery_admin_backend.user.enums.UserRole

@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.FUNCTION)
annotation class UserRights(val targetRoles: Array<UserRole> = [
    UserRole.SUPER_ADMIN,
    UserRole.ADMIN,
    UserRole.FRANCHISEE,
    UserRole.DRIVER
])
