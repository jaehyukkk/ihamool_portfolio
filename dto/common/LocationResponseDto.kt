package com.ilogistic.delivery_admin_backend.dto.common

data class LocationResponseDto(
    var latitude: Double? = null,
    var longitude: Double? = null,
    var driverId: Long? = null,
    var driverName: String? = null,
    var carNumber: String? = null,
    var carType: String? = null,
    var adminGroupCode: String? = null,
) {

    companion object{
        fun of(locationDto: LocationRequestDto) : LocationResponseDto {
            return LocationResponseDto(
                latitude = locationDto.latitude,
                longitude = locationDto.longitude,
                driverId = locationDto.driverId,
                driverName = locationDto.driverName,
                carNumber = locationDto.driverCarNumber,
                carType = locationDto.driverCarType,
                adminGroupCode = locationDto.driverAdminGroupCode
            )
        }
    }
}
