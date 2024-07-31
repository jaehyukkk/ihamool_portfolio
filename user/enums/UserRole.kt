package com.ilogistic.delivery_admin_backend.user.enums

enum class UserRole(val value: String) {
    SUPER_ADMIN("ROLE_SUPER_ADMIN"),
    ADMIN("ROLE_ADMIN"),
    FRANCHISEE("ROLE_FRANCHISEE"),
    DRIVER("ROLE_DRIVER"),
    CALL_WORKER("ROLE_CALL_WORKER");

    companion object {
        fun fromValue(value: String): UserRole {
            return when (value) {
                "ROLE_SUPER_ADMIN" -> SUPER_ADMIN
                "ROLE_ADMIN" -> ADMIN
                "ROLE_FRANCHISEE" -> FRANCHISEE
                "ROLE_DRIVER" -> DRIVER
                "CALL_WORKER" -> CALL_WORKER
                else -> throw IllegalArgumentException("Unknown value: $value")
            }
        }
    }
}
