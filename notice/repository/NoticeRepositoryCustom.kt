package com.ilogistic.delivery_admin_backend.notice.repository

import com.ilogistic.delivery_admin_backend.notice.domain.dto.NoticeDetailResponseDto
import com.ilogistic.delivery_admin_backend.notice.domain.dto.NoticeListResponseDto
import com.ilogistic.delivery_admin_backend.notice.domain.dto.NoticeSearchDto
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable

interface NoticeRepositoryCustom {
    fun getNoticePage(pageable: Pageable, noticeSearchDto: NoticeSearchDto) : Page<NoticeListResponseDto>

    fun getNoticeDetail(id : Long) : NoticeDetailResponseDto
}
