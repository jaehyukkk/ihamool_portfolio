package com.ilogistic.delivery_admin_backend.dispatch.service

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import com.ilogistic.delivery_admin_backend.api.service.ApiService
import com.ilogistic.delivery_admin_backend.cartype.service.CarTypeService
import com.ilogistic.delivery_admin_backend.dispatch.domain.dto.*
import com.ilogistic.delivery_admin_backend.dispatch.domain.entity.Dispatch
import com.ilogistic.delivery_admin_backend.dispatch.domain.repository.DispatchRepository
import com.ilogistic.delivery_admin_backend.dispatch.enums.DispatchStatus
import com.ilogistic.delivery_admin_backend.dispatch.enums.WebSocketSubTopic
import com.ilogistic.delivery_admin_backend.dispatch.enums.WebSocketTopic
import com.ilogistic.delivery_admin_backend.dispatchcartype.service.DispatchCarTypeService
import com.ilogistic.delivery_admin_backend.dispatchcomplete.service.DispatchCompleteService
import com.ilogistic.delivery_admin_backend.dispatchlogging.service.DispatchLoggingService
import com.ilogistic.delivery_admin_backend.dto.common.PaginateDto
import com.ilogistic.delivery_admin_backend.exception.BaseException
import com.ilogistic.delivery_admin_backend.exception.CustomMessageRuntimeException
import com.ilogistic.delivery_admin_backend.exception.ErrorCode
import com.ilogistic.delivery_admin_backend.handler.WebSocketHandler
import com.ilogistic.delivery_admin_backend.message.domain.dto.MessageRequestDto
import com.ilogistic.delivery_admin_backend.message.enums.MessageType
import com.ilogistic.delivery_admin_backend.message.service.MessageService
import com.ilogistic.delivery_admin_backend.user.domain.entity.Driver
import com.ilogistic.delivery_admin_backend.user.domain.entity.User
import com.ilogistic.delivery_admin_backend.user.enums.UserRole
import com.ilogistic.delivery_admin_backend.user.service.UserService
import com.ilogistic.delivery_admin_backend.utils.TimeUtil
import com.ilogistic.delivery_admin_backend.utils.Utils
import org.springframework.data.domain.Page
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime
import java.util.*
import kotlin.collections.ArrayList
import kotlin.math.floor

@Service
class DispatchService(
    private val dispatchRepository: DispatchRepository,
    private val userService: UserService,
    private val apiService: ApiService,
    private val webSocketHandler: WebSocketHandler,
    private val dispatchLoggingService: DispatchLoggingService,
    private val dispatchCompleteService: DispatchCompleteService,
    private val dispatchCarTypeService: DispatchCarTypeService,
    private val carTypeService: CarTypeService,
    private val messageService: MessageService
) {


    @Transactional
    fun create(dispatchRequestDto: DispatchRequestDto, userId : Long) {

        val user = userService.getUser(userId)
        dispatchRequestDto.user = user
        dispatchRequestDto.finalApprovalUser = user
        dispatchRequestDto.adminGroup = user.adminGroup

        dispatchRequestDto.parentCarType = dispatchRequestDto.parentCarTypeId.let { carTypeService.detail(it) }
        val dispatchCode : String = Utils.generateDispatchCode()
        //직권기사를 선택했을 경우
        if(dispatchRequestDto.forceDriverId != null){
            dispatchRequestDto.status = DispatchStatus.DISPATCHED
            dispatchRequestDto.driver = userService.getDriverEntity(dispatchRequestDto.forceDriverId)
            dispatchRequestDto.dispatchDateTime = LocalDateTime.now()
        }
        val dispatch = dispatchRepository.save(dispatchToEntity(dispatchRequestDto, dispatchCode))


        dispatchCarTypeService.create(dispatch, dispatchRequestDto.carTypeIds)

        var webSocketSendDto : WebSocketSendDto? = null
        if(dispatchRequestDto.forceDriverId == null){
            val channels = getDefaultWebSocketSendDto(dispatchRequestDto.carTypeIds, dispatchRequestDto.adminGroup!!.groupCode)

            webSocketSendDto = WebSocketSendDto(
                data = getDispatchDetail(dispatch.id!!)!!,
                topic = WebSocketTopic.NEW_ORDER_TOPIC,
                channels = channels
            )
        } else {
            val channels = mutableListOf<WebSocketSendDto.Companion.Channel>()

            channels.add(WebSocketSendDto.Companion.Channel(
                name = "driver-c-${dispatchRequestDto.driver!!.carType!!.id!!}",
                targetIds = mutableListOf(dispatchRequestDto.driver!!.id!!)
            ))


            webSocketSendDto = WebSocketSendDto(
                data = getDispatchDetail(dispatch.id!!)!!,
                topic = WebSocketTopic.DISPATCH_FORCE_TOPIC,
                channels = channels
            )
        }

        messageService.sendMessage(
            null,
            MessageRequestDto(
            receiverIds = listOf(dispatchRequestDto.franchiseeId!!),
            message = "신규 배차가 등록되었습니다.",
            senderName = "시스템",
            messageType = MessageType.NOTIFICATION
        ))
        webSocketHandler.sendMessage(webSocketSendDto)

    }


    @Transactional
    fun franchiseeDispatchCreate(dispatchRequestDto: DispatchRequestDto, userId : Long) {

        val user = userService.getUser(userId)
        dispatchRequestDto.user = user
        dispatchRequestDto.adminGroup = user.adminGroup
        dispatchRequestDto.franchiseeId = userId
        dispatchRequestDto.status = DispatchStatus.REQUESTED

        dispatchRequestDto.parentCarType = dispatchRequestDto.parentCarTypeId.let { carTypeService.detail(it) }
        val dispatchCode : String = Utils.generateDispatchCode()
        val dispatch = dispatchRepository.save(dispatchToEntity(dispatchRequestDto, dispatchCode))

        dispatchCarTypeService.create(dispatch, dispatchRequestDto.carTypeIds)

        val webSocketSendDto = WebSocketSendDto(
            data = getDispatchDetail(dispatch.id!!)!!,
            topic = WebSocketTopic.REQUEST_ORDER_TOPIC,
            channels = mutableListOf(WebSocketSendDto.Companion.Channel(
                name = "admin-g-${dispatchRequestDto.adminGroup!!.groupCode}",
            ))
        )
        webSocketHandler.sendMessage(webSocketSendDto)


    }


    @Transactional
    fun modify(id: Long, dispatchRequestDto: DispatchRequestDto){
        val dispatchInfo = getDispatchDetail(id) ?: throw BaseException(ErrorCode.ENTITY_NOT_FOUND)
        if(dispatchInfo.status != DispatchStatus.WAITING){
            throw BaseException(ErrorCode.NOT_MODIFIABLE_STATUS)
        }
        val startCoordinateMap = apiService.getAddressCoordinates(dispatchRequestDto.startAddress)
        val endCoordinateMap = apiService.getAddressCoordinates(dispatchRequestDto.endAddress)
        val distance = apiService.getDirections(startCoordinateMap, endCoordinateMap, dispatchRequestDto.ton)

        dispatchRequestDto.startLatitude = startCoordinateMap["lat"]
        dispatchRequestDto.startLongitude = startCoordinateMap["lon"]
        dispatchRequestDto.endLatitude = endCoordinateMap["lat"]
        dispatchRequestDto.endLongitude = endCoordinateMap["lon"]
        dispatchRequestDto.distance = distance

        if (dispatchRepository.modify(dispatchRequestDto, id) != 1) {
            throw BaseException(ErrorCode.INTERNAL_SERVER_ERROR)
        }


        val channels = getDefaultWebSocketSendDto(dispatchRequestDto.carTypeIds, getDispatchAdminGroupCode(id))

        val webSocketSendDto = WebSocketSendDto(
            data = getDispatchDetail(id)!!,
            topic = WebSocketTopic.MODIFY_ORDER_TOPIC,
            channels = channels
        )

        messageService.sendMessage(
            null,
            MessageRequestDto(
                receiverIds = listOf(dispatchRequestDto.franchiseeId!!),
                message = "배차번호 ${dispatchInfo.dispatchCode}의 배차정보가 수정되었습니다.",
                senderName = "시스템",
                messageType = MessageType.NOTIFICATION
            ))

        webSocketHandler.sendMessage(webSocketSendDto)
    }

    private fun dispatchToEntity(dispatchRequestDto : DispatchRequestDto, dispatchCode : String) : Dispatch{
        val franchisee = userService.getFranchiseeEntity(dispatchRequestDto.franchiseeId!!)
        val finalApprovalUser : User? = dispatchRequestDto.finalApprovalUser
        val isViaAddress = dispatchRequestDto.viaAddress.isNullOrBlank().not()
        val startCoordinateMap = apiService.getAddressCoordinates(dispatchRequestDto.startAddress)
        val endCoordinateMap = apiService.getAddressCoordinates(dispatchRequestDto.endAddress)
        val viaCoordinateMap = if(isViaAddress) apiService.getAddressCoordinates(dispatchRequestDto.viaAddress!!) else null


        val distance = apiService.getDirections(startCoordinateMap, endCoordinateMap, dispatchRequestDto.ton)
        dispatchRequestDto.distance = distance
        dispatchRequestDto.franchisee = franchisee
        dispatchRequestDto.finalApprovalUser = finalApprovalUser
        dispatchRequestDto.startLatitude = startCoordinateMap["lat"]
        dispatchRequestDto.startLongitude = startCoordinateMap["lon"]
        dispatchRequestDto.endLatitude = endCoordinateMap["lat"]
        dispatchRequestDto.endLongitude = endCoordinateMap["lon"]
        dispatchRequestDto.viaLatitude = viaCoordinateMap?.get("lat")
        dispatchRequestDto.viaLongitude = viaCoordinateMap?.get("lon")

        return dispatchRequestDto.toEntity(dispatchCode)
    }


    fun getDispatchRequestPageList(paginateDto: PaginateDto, dispatchRequestSearchDto: DispatchRequestSearchDto, userId : Long): Page<DispatchRequestResponseDto> {
        return dispatchRepository.getDispatchRequestPageList(paginateDto.pageable(), dispatchRequestSearchDto, userId)
    }
    fun getDispatchList(): List<DispatchResponseDto>? {
        return dispatchRepository.getDispatchList()
    }

    fun getCompanyWaitDispatchList(id: Long): List<DispatchResponseDto>? {
        return dispatchRepository.getCompanyWaitDispatchList(id)
    }

    fun getDispatchWaitingListMatchingCarType(driverId: Long): List<DispatchResponseDto>? {
        return dispatchRepository.getDispatchWaitingListMatchingCarType(driverId)
    }

    fun getDispatchPageList(
        paginateDto: PaginateDto,
        dispatchSearchDto: DispatchSearchDto,
    ): Page<DispatchPageListResponseDto> {
        return dispatchRepository.getDispatchPageList(paginateDto.pageable(), dispatchSearchDto)
    }

    fun getDispatchPageListStatistics(dispatchSearchDto: DispatchSearchDto) : DispatchStatisticsResponseDto {
        return dispatchRepository.getDispatchPageListStatistics(dispatchSearchDto) ?: DispatchStatisticsResponseDto(0, 0, 0)

    }


    fun getDispatchDetail(id: Long): DispatchResponseDto? {
        return dispatchRepository.getDispatchDetail(id)
    }

    fun getDispatchDetailV2(id: Long): DispatchResponseDtoV2? {
        return dispatchRepository.getDispatchDetailV2(id)
    }


    fun getDispatchModifyDetail(id: Long): DispatchModifyResponseDto? {
        return dispatchRepository.getDispatchModifyDetail(id) ?: throw BaseException(ErrorCode.ENTITY_NOT_FOUND)
    }

    @Transactional
    fun dispatchRequest(dispatchId: Long, driverId: Long) {
        val driver = userService.getDriverEntity(driverId)
        val result = dispatchRepository.dispatchRequest(
            id = dispatchId,
            status = DispatchStatus.DISPATCHED,
            driver = driver
        )

        if (result != 1) {
            throw BaseException(ErrorCode.INTERNAL_SERVER_ERROR)
        }

        val dispatch = dispatchRepository.getDispatchSocketSendInfoDto(dispatchId)

       val channels = getDefaultWebSocketSendDto(dispatch.carTypeList!!.map{ it.id }, dispatch.adminGroupCode)

        val webSocketSendDto = WebSocketSendDto(
            data = mapOf("dispatchId" to dispatchId, "driverId" to driverId),
            topic = WebSocketTopic.DISPATCH_ORDER_TOPIC,
            channels = channels
        )

        messageService.sendMessage(
            null,
            MessageRequestDto(
                receiverIds = listOf(dispatch.franchiseeId),
                message = "배차번호 ${dispatch.dispatchCode}의 운송건이 ${driver.name}님에게 배차되었습니다.",
                senderName = "시스템",
                messageType = MessageType.NOTIFICATION
            ))
        webSocketHandler.sendMessage(webSocketSendDto)

        dispatchLoggingService.logging(driverId, dispatchId, DispatchStatus.DISPATCHED)
    }

    @Transactional
    fun forceDispatch(dispatchId: Long, driverId: Long) {
        val driver = userService.getDriverEntity(driverId)
        val result = dispatchRepository.dispatchRequest(
            id = dispatchId,
            status = DispatchStatus.DISPATCHED,
            driver = driver
        )
        if (result != 1) {
            throw BaseException(ErrorCode.INTERNAL_SERVER_ERROR)
        }

        val dispatch = dispatchRepository.getDispatchSocketSendInfoDto(dispatchId)

        val webSocketSendDto = WebSocketSendDto(
            data = mapOf("id" to dispatchId, "driverId" to driverId, "dispatchData" to getDispatchDetail(dispatchId)!!),
            topic = WebSocketTopic.DISPATCH_ORDER_TOPIC,
            subTopic = WebSocketSubTopic.FORCE,
            channels = getDefaultWebSocketSendDto(dispatch.carTypeList!!.map{ it.id }, dispatch.adminGroupCode)
        )

        messageService.sendMessage(
            null,
            MessageRequestDto(
                receiverIds = listOf(dispatch.franchiseeId),
                message = "배차번호 ${dispatch.dispatchCode}의 운송건이 ${driver.name}님에게 배차되었습니다.",
                senderName = "시스템",
                messageType = MessageType.NOTIFICATION
            ))

        webSocketHandler.sendMessage(webSocketSendDto)
        dispatchLoggingService.logging(driverId, dispatchId, DispatchStatus.DISPATCHED, isForce = true)
    }

    fun getDispatchListByStatusAndDriver(status: DispatchStatus, driverId: Long): List<DispatchResponseDto>? {
        return dispatchRepository.getDispatchListByStatusAndDriver(status, driverId)
    }

    fun getActiveDispatchList(driverId: Long): List<DispatchResponseDto> {
        return dispatchRepository.getActiveDispatchList(driverId) ?: ArrayList()
    }

    fun getDispatchCount(driverId: Long): DispatchCountResponseDto {
        return DispatchCountResponseDto(
            dispatchRepository.getDispatchCount(null, DispatchStatus.WAITING) ?: 0,
            dispatchRepository.getDispatchCount(driverId, DispatchStatus.DISPATCHED) ?: 0,
            dispatchRepository.getDispatchCount(driverId, DispatchStatus.COMPLETED) ?: 0
        )
    }

    //유저가 직접 배차 취소
    @Transactional
    fun dispatchCancel(driverId: Long, dispatchId: Long) {
        //취소할 권한이 있는지 확인
        val dispatch = dispatchRepository.getCheckDispatch(dispatchId, driverId)
            ?: throw BaseException(ErrorCode.FORBIDDEN)

        //배차 상태가 이미 운송을 했거나 완료 했으면 취소 불가
        if (dispatch.status != DispatchStatus.DISPATCHED) {
            throw BaseException(ErrorCode.NOT_MODIFIABLE_STATUS)
        }

        //배차 요청 후 5분이 지나면 취소 불가
        if (TimeUtil.isNMinutesPassed(dispatch.dispatchDateTime, 5)) {
            throw BaseException(ErrorCode.TIMEOUT)
        }

        val cancelCount: Long =
            dispatchLoggingService.getDriverCancelCount(driverId, dispatchId, DispatchStatus.DISPATCH_CANCELLED) ?: 0L

        //취소 횟수가 2회 이상이면 취소 불가
        if (cancelCount >= 2L) {
            throw BaseException(ErrorCode.EXCEED_VALID_COUNT)
        }

        if (dispatchRepository.dispatchCancel(dispatchId) != 1) {
            throw BaseException(ErrorCode.INTERNAL_SERVER_ERROR)
        }

        val dispatchInfo = dispatchRepository.getDispatchSocketSendInfoDto(dispatchId)
        val webSocketSendDto = WebSocketSendDto(
            data = mapOf("data" to getDispatchDetail(dispatchId)!!, "driverId" to driverId),
            topic = WebSocketTopic.DISPATCH_CANCEL_TOPIC,
            channels = getDefaultWebSocketSendDto(dispatchInfo.carTypeList!!.map{ it.id }, dispatchInfo.adminGroupCode)
        )

        messageService.sendMessage(
            null,
            MessageRequestDto(
                receiverIds = listOf(dispatchInfo.franchiseeId),
                message = "배차번호 ${dispatchInfo.dispatchCode} 운송건이 ${userService.getDriverEntity(driverId).name}님에 의해 취소되었습니다.",
                senderName = "시스템",
                messageType = MessageType.NOTIFICATION
            ))

        dispatchLoggingService.logging(driverId, dispatchId, DispatchStatus.DISPATCH_CANCELLED)
        webSocketHandler.sendMessage(webSocketSendDto)
    }

    //관리자가 강제 배차 취소
    fun forceDispatchCancel(dispatchId: Long, driverId: Long) {

        val dispatch = dispatchRepository.getDispatchSocketSendInfoDto(dispatchId)
        val dispatchStatus = dispatch.status
        if (!(dispatchStatus == DispatchStatus.DISPATCHED || dispatchStatus == DispatchStatus.LOADING)) {
            throw BaseException(ErrorCode.NOT_MODIFIABLE_STATUS)
        }

        if (dispatchRepository.dispatchCancel(dispatchId) != 1) {
            throw BaseException(ErrorCode.INTERNAL_SERVER_ERROR)
        }

        val webSocketSendDto = WebSocketSendDto(
//            data = getDispatchDetail(dispatchId)!!,
            data = mapOf("data" to getDispatchDetail(dispatchId)!!, "driverId" to driverId),
            topic = WebSocketTopic.DISPATCH_CANCEL_TOPIC,
            subTopic = WebSocketSubTopic.FORCE,
            channels = getDefaultWebSocketSendDto(dispatch.carTypeList!!.map{ it.id }, dispatch.adminGroupCode)
        )

        messageService.sendMessage(
            null,
            MessageRequestDto(
                receiverIds = listOf(dispatch.franchiseeId, driverId),
                message = "배차번호 ${dispatch.dispatchCode} 운송건이 관리자에 의해 취소되었습니다.",
                senderName = "시스템",
                messageType = MessageType.NOTIFICATION
            ))

        webSocketHandler.sendMessage(webSocketSendDto)
    }

    @Transactional
    fun dispatchLoading(dispatchId: Long, driverId: Long) {

        dispatchRepository.getCheckDispatch(dispatchId, driverId)
            ?: throw BaseException(ErrorCode.BAD_REQUEST)

        val result = dispatchRepository.dispatchLoading(dispatchId, DispatchStatus.LOADING)

        if (result != 1) {
            throw BaseException(ErrorCode.INTERNAL_SERVER_ERROR)
        }

        val dispatch = dispatchRepository.getDispatchSocketSendInfoDto(dispatchId)

        messageService.sendMessage(
            null,
            MessageRequestDto(
                receiverIds = listOf(dispatch.franchiseeId),
                message = "배차번호 ${dispatch.dispatchCode} 운송건을 ${dispatch.driverName} 기사님이 상차하였습니다.",
                senderName = "시스템",
                messageType = MessageType.NOTIFICATION
            ))


        dispatchLoggingService.logging(driverId, dispatchId, DispatchStatus.LOADING)
    }


    fun getDispatchEntity(dispatchId: Long): Optional<Dispatch> {
        return dispatchRepository.findById(dispatchId)
    }

    @Transactional
    fun dispatchComplete(dispatchId: Long, driverId: Long) {
        val dispatch = getDispatchEntity(dispatchId).orElseThrow {
            throw BaseException(ErrorCode.BAD_REQUEST)
        }

        val driver: Driver = userService.getDriverEntity(driverId)

        if (!(dispatch.status == DispatchStatus.DISPATCHED || dispatch.status == DispatchStatus.LOADING)) {
            throw BaseException(ErrorCode.BAD_REQUEST)
        }

        val result = dispatchRepository.dispatchStatusChange(dispatchId, DispatchStatus.COMPLETED)

        if (result != 1) {
            throw BaseException(ErrorCode.INTERNAL_SERVER_ERROR)
        }

//        val userLastPoint = userPointService.getDriverLastPoint(driverId)
//        val userPoint : UserPoint = UserPoint(
//            driver = userService.getDriverEntity(driverId),
//            pointReason = PointReason.DELIVERY_COMPLETE,
//            currentPoint = userLastPoint + dispatch.driverPrice,
//            awardedPoint = dispatch.driverPrice,
//        )
//        userPointService.updatePoint(userPoint)

        messageService.sendMessage(
            null,
            MessageRequestDto(
                receiverIds = listOf(dispatch.franchisee!!.id!!),
                message = "배차번호 ${dispatch.dispatchCode} 운송건이 완료되었습니다.",
                senderName = "시스템",
                messageType = MessageType.NOTIFICATION
            ))


        dispatchLoggingService.logging(driverId, dispatchId, DispatchStatus.COMPLETED)
        dispatchCompleteService.save(
            dispatch = dispatch,
            driver = driver
        )
    }

    private fun getDispatchStatus(id: Long) : DispatchStatus{
        return dispatchRepository.getDispatchStatus(id) ?: throw BaseException(ErrorCode.BAD_REQUEST)
    }

//    Math.floor(inputs.originalPrice - inputs.shipPrice - inputs.originalPrice * (inputs.fee / 100))

    @Transactional
    fun delete(id: List<Long>) {
        for(i in id){
            if (getDispatchStatus(i) != DispatchStatus.WAITING) {
                throw BaseException(ErrorCode.NOT_DELETE_STATUS)
            }
            dispatchRepository.deleteById(i)
        }
    }

    //화주가 요청한 배차 승인
    @Transactional
    fun dispatchApprove(dispatchRequestApproveRequestDtoList: List<DispatchRequestApproveRequestDto>, userId: Long) {
        val dispatchList: MutableList<Dispatch> = mutableListOf<Dispatch>()
        val finalApprovalUser = userService.getUser(userId)
        val adminGroupCode = finalApprovalUser.adminGroup!!.groupCode
        for (dispatchRequestApproveRequestDto in dispatchRequestApproveRequestDtoList) {
            val dispatch = dispatchRepository.findById(dispatchRequestApproveRequestDto.id)
                .orElseThrow { throw BaseException(ErrorCode.ENTITY_NOT_FOUND) }

            val driverPrice = getDriverPrice(
                dispatch.originalPrice,
                dispatchRequestApproveRequestDto.shipPrice,
                dispatchRequestApproveRequestDto.fee
            )

            val result : Dispatch = dispatchRepository.save(dispatch.dispatchApprove(dispatchRequestApproveRequestDto, driverPrice, finalApprovalUser))

            dispatchList.add(result)
        }

        dispatchList.forEach{
            val channels = getDefaultWebSocketSendDto(
                carTypeIds = it.dispatchCarTypes.map{ carType -> carType.carType.id!!},
                adminGroupCode = adminGroupCode
            )
            val webSocketSendDto = WebSocketSendDto(
                data = getDispatchDetail(it.id!!)!!,
                topic = WebSocketTopic.NEW_ORDER_TOPIC,
                channels = channels
            )

            messageService.sendMessage(
                null,
                MessageRequestDto(
                    receiverIds = listOf(it.franchisee!!.id!!),
                    message = "배차번호 ${it.dispatchCode} 운송건이 승인되었습니다.",
                    senderName = "시스템",
                    messageType = MessageType.NOTIFICATION
                ))

            webSocketHandler.sendMessage(webSocketSendDto)
        }
    }

    fun isDriverRunningBatch(driverId: Long, franchiseeId: Long) : Boolean {
        return dispatchRepository.isDriverRunningBatch(driverId, franchiseeId)
    }

    fun getFranchiseeDispatchDriverIdList(franchiseeId: Long): List<Long> {
        return dispatchRepository.getFranchiseeDispatchDriverIdList(franchiseeId) ?: ArrayList()
    }

    fun getDispatchAdminGroupCode(id: Long): String {
        return dispatchRepository.getDispatchAdminGroupCode(id) ?: throw BaseException(ErrorCode.ENTITY_NOT_FOUND)
    }

    //기사 지급 원가 구하는 함수
    fun getDriverPrice(originalPrice: Int, shipPrice: Int, fee: Int): Int {
        return floor(originalPrice - shipPrice - originalPrice * (fee.toDouble() / 100)).toInt()
    }

    fun test() : ApprovalUserTestDto{
        return dispatchRepository.test(3) ?: throw BaseException(ErrorCode.ENTITY_NOT_FOUND)
    }

    private fun getDefaultWebSocketSendDto(carTypeIds: List<Long>, adminGroupCode: String) : MutableList<WebSocketSendDto.Companion.Channel> {
        val channels = carTypeIds.map { carTypeId ->
            WebSocketSendDto.Companion.Channel(
                name = "driver-c-${carTypeId}",
            )
        }.toMutableList()

        channels.add(WebSocketSendDto.Companion.Channel(
            name = "admin-g-${adminGroupCode}",
        ))

        return channels
    }

    fun getFranchiseeMainScreenDispatchList(franchiseeId: Long): List<FranchiseeDispatchMainScreenResponseDto>? {
        return dispatchRepository.getFranchiseeMainScreenDispatchList(franchiseeId)
    }
}
