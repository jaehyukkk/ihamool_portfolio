package com.ilogistic.delivery_admin_backend.api.service

import com.baroservice.api.BarobillApiService
import com.baroservice.ws.ArrayOfTaxInvoiceTradeLineItem
import com.baroservice.ws.InvoiceParty
import com.baroservice.ws.TaxInvoice
import com.baroservice.ws.TaxInvoiceTradeLineItem
import com.ilogistic.delivery_admin_backend.admingroup.service.AdminGroupService
import com.ilogistic.delivery_admin_backend.api.domain.dto.TaxInvoiceRequestDto
import com.ilogistic.delivery_admin_backend.applicationfeepayment.service.ApplicationFeePaymentService
import com.ilogistic.delivery_admin_backend.dispatchcomplete.service.DispatchCompleteService
import com.ilogistic.delivery_admin_backend.exception.BaseException
import com.ilogistic.delivery_admin_backend.exception.ErrorCode
import com.ilogistic.delivery_admin_backend.user.service.UserService
import com.ilogistic.delivery_admin_backend.userpoint.domain.dto.UserPointRequestDto
import com.ilogistic.delivery_admin_backend.userpoint.domain.entity.UserPoint
import com.ilogistic.delivery_admin_backend.userpoint.enums.PointReason
import com.ilogistic.delivery_admin_backend.userpoint.service.UserPointService
import org.springframework.stereotype.Service

@Service
class BarobillService(
    private val barobillApiService: BarobillApiService,
    private val dispatchCompleteService: DispatchCompleteService,
    private val userPointService: UserPointService,
    private val applicationFeePaymentService: ApplicationFeePaymentService
) {

//    taxInvoice.brokerParty.mgtNum = "000002"
    private fun getMgtNum(dispatchId: Long): String {
        //dispatchid 가 1이면 mgtNum은 000001
        return String.format("%06d", dispatchId)
    }
    fun registAndIssueBrokerTaxInvoice(dispatchId: Long, userId: Long) {
        val currentPoint = userPointService.getUserPoint(userId)
        if (currentPoint < 500) {
            throw BaseException(ErrorCode.POINT_NOT_ENOUGH)
        }
        val taxInvoiceRequestDto : TaxInvoiceRequestDto = dispatchCompleteService.getTaxInvoiceInfo(dispatchId, userId)
        val amount = taxInvoiceRequestDto.amount
        val tax = (amount * 0.1).toInt()
        val totalAmount = (amount + tax)


        val certKey = "853536F3-5E32-455E-B23E-DEF9751B4E7A"
        val taxInvoice = TaxInvoice()
        taxInvoice.issueDirection = 1
        taxInvoice.taxInvoiceType = 4
        taxInvoice.modifyCode = ""
        taxInvoice.taxType = 1
        taxInvoice.taxCalcType = 1
        taxInvoice.purposeType = 2
        taxInvoice.writeDate = ""
        taxInvoice.amountTotal = amount.toString()
        taxInvoice.taxTotal = tax.toString()
        taxInvoice.totalAmount = totalAmount.toString()
        taxInvoice.cash = ""
        taxInvoice.chkBill = ""
        taxInvoice.note = ""
        taxInvoice.credit = ""
        taxInvoice.remark1 = ""
        taxInvoice.remark2 = ""
        taxInvoice.remark3 = ""
        taxInvoice.kwon = ""
        taxInvoice.ho = ""
        taxInvoice.serialNum = ""

        // 공급자 정보
        taxInvoice.invoicerParty = InvoiceParty()
        taxInvoice.invoicerParty.mgtNum = ""
        //사업자번호
        taxInvoice.invoicerParty.corpNum = taxInvoiceRequestDto.driverCompanyNumber
        taxInvoice.invoicerParty.taxRegID = ""
        taxInvoice.invoicerParty.corpName = taxInvoiceRequestDto.driverCompanyName
        taxInvoice.invoicerParty.ceoName = taxInvoiceRequestDto.driverName
        taxInvoice.invoicerParty.addr = taxInvoiceRequestDto.driverAddress + " " + taxInvoiceRequestDto.driverDetailAddress
        taxInvoice.invoicerParty.bizType = "운수업"
        taxInvoice.invoicerParty.bizClass = "화물"
        taxInvoice.invoicerParty.contactID = ""
        taxInvoice.invoicerParty.contactName = "장재혁"
        taxInvoice.invoicerParty.tel = ""
        taxInvoice.invoicerParty.hp = ""
        taxInvoice.invoicerParty.email = "wogur11178@gmail.com"

        //공급받는자 정보
        taxInvoice.invoiceeParty = InvoiceParty()
        taxInvoice.invoiceeParty.corpNum = taxInvoiceRequestDto.adminGroupCompanyNumber
        taxInvoice.invoiceeParty.taxRegID = ""
        taxInvoice.invoiceeParty.corpName = taxInvoiceRequestDto.adminGroupCompanyName
        taxInvoice.invoiceeParty.ceoName = taxInvoiceRequestDto.adminGroupCeoName
        taxInvoice.invoiceeParty.addr = taxInvoiceRequestDto.adminGroupAddress + " " + taxInvoiceRequestDto.adminGroupDetailAddress
        taxInvoice.invoiceeParty.bizType = "운수 및 창고업"
        taxInvoice.invoiceeParty.bizClass = "기타 도로 화물 운송업"
        taxInvoice.invoiceeParty.contactID = ""
        taxInvoice.invoiceeParty.contactName = "장재혁"
        taxInvoice.invoiceeParty.tel = ""
        taxInvoice.invoiceeParty.hp = ""
        taxInvoice.invoiceeParty.email = ""

        // 수탁자 정보
        taxInvoice.brokerParty = InvoiceParty()
        taxInvoice.brokerParty.corpNum = taxInvoiceRequestDto.adminGroupCompanyNumber
        taxInvoice.brokerParty.taxRegID = ""
        //회사명
        taxInvoice.brokerParty.corpName = taxInvoiceRequestDto.adminGroupCompanyName
        //대표자명
        taxInvoice.brokerParty.ceoName = taxInvoiceRequestDto.adminGroupCeoName
        taxInvoice.brokerParty.addr = taxInvoiceRequestDto.adminGroupAddress + " " + taxInvoiceRequestDto.adminGroupDetailAddress
        //업태
        taxInvoice.brokerParty.bizType = "운수 및 창고업"
        taxInvoice.brokerParty.bizClass = "기타 도로 화물 운송업"
        taxInvoice.brokerParty.contactID = taxInvoiceRequestDto.barobillId ?: throw BaseException(ErrorCode.BAROBILL_ID_NOT_FOUND)
        taxInvoice.brokerParty.contactName = "장재혁"
        taxInvoice.brokerParty.tel = ""
        taxInvoice.brokerParty.hp = ""
        taxInvoice.brokerParty.email = "wogur11178@gmail.com"
        taxInvoice.brokerParty.mgtNum = getMgtNum(dispatchId)
//        taxInvoice.brokerParty.corpNum = "4148109169"
//        taxInvoice.brokerParty.taxRegID = ""
//        //회사명
//        taxInvoice.brokerParty.corpName = "아이로지스틱"
//        //대표자명
//        taxInvoice.brokerParty.ceoName = "김은비"
//        taxInvoice.brokerParty.addr = "경기도 화성시 동탄기흥로 570-6 9층 13, 14, 15호"
//        //업태
//        taxInvoice.brokerParty.bizType = "운수 및 창고업"
//        taxInvoice.brokerParty.bizClass = "기타 도로 화물 운송업"
//        taxInvoice.brokerParty.contactID = "wogur1178"
//        taxInvoice.brokerParty.contactName = "장재혁"
//        taxInvoice.brokerParty.tel = ""
//        taxInvoice.brokerParty.hp = ""
//        taxInvoice.brokerParty.email = "wogur11178@gmail.com"
//        taxInvoice.brokerParty.mgtNum = "000002"

        // 품목
        taxInvoice.taxInvoiceTradeLineItems = ArrayOfTaxInvoiceTradeLineItem()
//        for (i in 0..1) {
        val taxInvoiceTradeLineItem = TaxInvoiceTradeLineItem()
        taxInvoiceTradeLineItem.purchaseExpiry = ""
        taxInvoiceTradeLineItem.name = ""
        taxInvoiceTradeLineItem.information = ""
        taxInvoiceTradeLineItem.chargeableUnit = ""
        taxInvoiceTradeLineItem.unitPrice = ""
        taxInvoiceTradeLineItem.amount = amount.toString()
        taxInvoiceTradeLineItem.tax = tax.toString()
        taxInvoiceTradeLineItem.description = ""
        taxInvoice.taxInvoiceTradeLineItems.taxInvoiceTradeLineItem.add(taxInvoiceTradeLineItem)
//        }
        val sendSms = false
        val forceIssue = false
        val mailTitle = ""
        val result = barobillApiService.taxInvoice.registAndIssueBrokerTaxInvoice(
            certKey,
            taxInvoice.brokerParty.corpNum,
            taxInvoice,
            sendSms,
            forceIssue,
            mailTitle
        )
        if (result < 0) { // 호출 실패
            throw BaseException(ErrorCode.TAX_INVOICE_FAIL)
        } else {
            val userPoint = userPointService.deductPoint(
                UserPointRequestDto(
                    reason = PointReason.TAX_INVOICE,
                    userId = userId,
                    point = 500,
                )
            )
            applicationFeePaymentService.paymentV2(userId, userPoint)
            dispatchCompleteService.editTaxInvoiceInfo(dispatchId)
            println("SUCCESS")
        }
    }
}
