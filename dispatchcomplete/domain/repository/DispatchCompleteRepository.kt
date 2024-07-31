package com.ilogistic.delivery_admin_backend.dispatchcomplete.domain.repository

import com.ilogistic.delivery_admin_backend.dispatchcomplete.domain.entity.DispatchComplete
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime

@Repository
interface DispatchCompleteRepository : JpaRepository<DispatchComplete, Long>, DispatchCompleteRepositoryCustom {

    @Transactional
    @Modifying
    @Query("UPDATE DispatchComplete d SET d.isTaxInvoice = :isTaxInvoice, d.taxInvoiceIssueDate = :date  WHERE d.id = :id")
    fun editTaxInvoiceInfo(id : Long, isTaxInvoice : Boolean? = true, date : LocalDateTime? = LocalDateTime.now())

}
