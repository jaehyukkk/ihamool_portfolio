package com.ilogistic.delivery_admin_backend.notice.service

import com.ilogistic.delivery_admin_backend.dto.common.PaginateDto
import com.ilogistic.delivery_admin_backend.exception.BaseException
import com.ilogistic.delivery_admin_backend.exception.CustomMessageRuntimeException
import com.ilogistic.delivery_admin_backend.exception.CustomRuntimeException
import com.ilogistic.delivery_admin_backend.exception.ErrorCode
import com.ilogistic.delivery_admin_backend.notice.domain.dto.NoticeDetailResponseDto
import com.ilogistic.delivery_admin_backend.notice.domain.dto.NoticeListResponseDto
import com.ilogistic.delivery_admin_backend.notice.domain.dto.NoticeRequestDto
import com.ilogistic.delivery_admin_backend.notice.domain.dto.NoticeSearchDto
import com.ilogistic.delivery_admin_backend.notice.domain.entity.Notice
import com.ilogistic.delivery_admin_backend.notice.repository.NoticeRepository
import com.ilogistic.delivery_admin_backend.user.service.UserService
import org.springframework.data.domain.Page
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
class NoticeService(
    private val noticeRepository: NoticeRepository,
    private val userService: UserService
) {

    fun create(noticeRequestDto: NoticeRequestDto, userId : Long) : Notice {
        return noticeRepository.save(noticeRequestDto.toEntity(userService.getUser(userId)))
    }

    fun selectNoticePage(noticeSearchDto: NoticeSearchDto, paginateDto: PaginateDto) : Page<NoticeListResponseDto> {
        return noticeRepository.getNoticePage(paginateDto.pageable(), noticeSearchDto)
    }


    fun detail(id : Long) : NoticeDetailResponseDto {
        return noticeRepository.getNoticeDetail(id)
    }

    fun modify(id: Long, noticeRequestDto: NoticeRequestDto, userId : Long) {
        noticeRepository.modifyNotice(
            id = id,
            title = noticeRequestDto.title,
            content = noticeRequestDto.content,
            user = userService.getUser(userId),
            now = LocalDateTime.now()
        )
    }

    fun delete(ids: List<Long>) {
        for(id in ids) {
            noticeRepository.deleteById(id)
        }
    }

    fun hit(id: Long) {
        if (noticeRepository.modifyHit(id) != 1) {
            throw BaseException(ErrorCode.INTERNAL_SERVER_ERROR)
        }
    }
}
