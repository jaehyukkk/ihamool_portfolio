package com.ilogistic.delivery_admin_backend.dispatch.domain.repository

import com.ilogistic.delivery_admin_backend.dispatch.domain.dto.DispatchRequestDto
import com.ilogistic.delivery_admin_backend.dispatch.domain.entity.Dispatch
import com.ilogistic.delivery_admin_backend.dispatch.enums.DispatchStatus
import com.ilogistic.delivery_admin_backend.user.domain.entity.Driver
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime

@Repository
interface DispatchRepository : JpaRepository<Dispatch, Long>, DispatchRepositoryCustom{

    @Modifying
    @Transactional
    @Query("UPDATE Dispatch d SET d.status = :status, d.driver = :driver, d.dispatchDateTime = :dispatchDateTime WHERE d.id = :id")
    fun dispatchRequest(id: Long, status: DispatchStatus, driver: Driver, dispatchDateTime: LocalDateTime? = LocalDateTime.now()): Int

    @Modifying
    @Transactional
    @Query("UPDATE Dispatch d SET d.status = :status, d.driver = null WHERE d.id = :id")
    fun dispatchCancel(id: Long, status: DispatchStatus? = DispatchStatus.WAITING): Int

    @Modifying
    @Transactional
    @Query("""
        UPDATE Dispatch d
        SET d.startAddress = :#{#dispatchRequestDto.startAddress},
            d.startDetailAddress = :#{#dispatchRequestDto.startDetailAddress},
            d.startZipCode = :#{#dispatchRequestDto.startZipCode},
            d.startLatitude = :#{#dispatchRequestDto.startLatitude},
            d.startLongitude = :#{#dispatchRequestDto.startLongitude},
            d.startSigungu = :#{#dispatchRequestDto.startSigungu},
            d.startSido = :#{#dispatchRequestDto.startSido},
            d.endAddress = :#{#dispatchRequestDto.endAddress},
            d.endDetailAddress = :#{#dispatchRequestDto.endDetailAddress},
            d.endZipCode = :#{#dispatchRequestDto.endZipCode},
            d.endLatitude = :#{#dispatchRequestDto.endLatitude},
            d.endLongitude = :#{#dispatchRequestDto.endLongitude},
            d.endSigungu = :#{#dispatchRequestDto.endSigungu},
            d.endSido = :#{#dispatchRequestDto.endSido},
            d.distance = :#{#dispatchRequestDto.distance},
            d.originalPrice = :#{#dispatchRequestDto.originalPrice},
            d.fee = :#{#dispatchRequestDto.fee},
            d.driverPrice = :#{#dispatchRequestDto.driverPrice},
            d.loadingMethod = :#{#dispatchRequestDto.loadingMethod},
            d.itemName = :#{#dispatchRequestDto.itemName},
            d.itemCount = :#{#dispatchRequestDto.itemCount},
            d.palletCount = :#{#dispatchRequestDto.palletCount},
            d.precautions = :#{#dispatchRequestDto.precautions},
            d.memo = :#{#dispatchRequestDto.memo},
            d.paymentType = :#{#dispatchRequestDto.paymentType},
            d.dispatchStart = :#{#dispatchRequestDto.dispatchStart},
            d.dispatchEnd = :#{#dispatchRequestDto.dispatchEnd}
        WHERE d.id = :id
    """)
    fun modify(dispatchRequestDto: DispatchRequestDto, id: Long): Int

    @Modifying
    @Transactional
    @Query("UPDATE Dispatch d SET d.status = :status, d.loadingDateTime = :loadingDateTime WHERE d.id = :id")
    fun dispatchLoading(id: Long, status: DispatchStatus, loadingDateTime: LocalDateTime? = LocalDateTime.now()): Int

    @Modifying
    @Transactional
    @Query("UPDATE Dispatch d SET d.status = :status WHERE d.id = :id")
    fun dispatchStatusChange(id: Long, status: DispatchStatus): Int

    @Modifying
    @Transactional
    @Query("UPDATE Dispatch d SET d.status = :status, d.fee = :fee, d.shipPrice = :shipPrice, d.driverPrice = :driverPrice WHERE d.id = :id")
    fun dispatchApprove(id: Long, status: DispatchStatus, fee : Int, shipPrice: Int, driverPrice: Int): Int


//    fun existsByIdAndDriverAndStatus(id: Long, driver: Driver, status: DispatchStatus): Boolean
}
