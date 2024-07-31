package com.ilogistic.delivery_admin_backend.user.repository.driver

import com.ilogistic.delivery_admin_backend.user.domain.dto.DriverSignupDto
import com.ilogistic.delivery_admin_backend.user.domain.entity.Driver
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional

@Repository
interface DriverRepository : JpaRepository<Driver, Long>, DriverRepositoryCustom{

    @Modifying
    @Transactional
    @Query("""
        UPDATE Driver d SET 
        d.name = :#{#driverSignupDto.name}
        , d.phone = :#{#driverSignupDto.phone}
        , d.bankNumber = :#{#driverSignupDto.bankNumber}
        , d.bank = :#{#driverSignupDto.bank}
        , d.bankOwner = :#{#driverSignupDto.bankOwner}
        , d.address = :#{#driverSignupDto.address}
        , d.detailAddress = :#{#driverSignupDto.detailAddress}
        , d.zipCode = :#{#driverSignupDto.zipCode}
        , d.carNumber = :#{#driverSignupDto.carNumber} 
        , d.carType = :#{#driverSignupDto.carType}
        , d.companyNumber = :#{#driverSignupDto.companyNumber}
        , d.companyName = :#{#driverSignupDto.companyName}
        , d.companyAddress = :#{#driverSignupDto.companyAddress}
        , d.companyDetailAddress = :#{#driverSignupDto.companyDetailAddress}
        WHERE d.id = :id""")
    fun modifyDriver(id : Long, driverSignupDto: DriverSignupDto): Int


    @Query("SELECT d FROM Driver d join fetch d.carType WHERE d.id = :id")
    fun findDriverById(id: Long): Driver?
}

