package com.ilogistic.delivery_admin_backend.dto.common

class LocationRequestDto(
    var latitude: Double? = null,
    var longitude: Double? = null,
    var driverId: Long? = null,
    var driverName: String? = null,
    var driverCarNumber: String? = null,
    var driverCarType: String? = null,
    var driverAdminGroupCode: String? = null,
) {
}
