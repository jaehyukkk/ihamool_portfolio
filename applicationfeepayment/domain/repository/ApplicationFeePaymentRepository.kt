package com.ilogistic.delivery_admin_backend.applicationfeepayment.domain.repository

import com.ilogistic.delivery_admin_backend.applicationfeepayment.domain.entity.ApplicationFeePayment
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.query.Procedure
import org.springframework.stereotype.Repository

@Repository
interface ApplicationFeePaymentRepository : JpaRepository<ApplicationFeePayment, Long>, ApplicationFeePaymentRepositoryCustom{

    @Procedure("ProcessApplicationFeePayments")
    fun paymentProcedure()
}
