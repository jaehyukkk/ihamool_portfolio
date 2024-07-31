package com.ilogistic.delivery_admin_backend.notice.controller

import com.ilogistic.delivery_admin_backend.notice.domain.dto.NoticeListResponseDto
import com.ilogistic.delivery_admin_backend.notice.domain.dto.NoticeRequestDto
import com.ilogistic.delivery_admin_backend.dto.common.PaginateDto
import com.ilogistic.delivery_admin_backend.notice.domain.dto.NoticeDetailResponseDto
import com.ilogistic.delivery_admin_backend.notice.domain.dto.NoticeSearchDto
import com.ilogistic.delivery_admin_backend.notice.domain.entity.Notice
import com.ilogistic.delivery_admin_backend.notice.service.NoticeService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.data.domain.Page
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import java.security.Principal

@Tag(name = "공지사항 관련 API")
@RestController
class NoticeController(
    private val noticeService: NoticeService
) {

    @Operation(summary = "공지사항 등록 API")
//    @com.ilogistic.delivery_admin_backend.aop.AdminRights
    @PostMapping("/api/v1/notice")
    fun create(@RequestBody noticeRequestDto: NoticeRequestDto, principal: Principal): ResponseEntity<Notice> {
        return ResponseEntity.ok(noticeService.create(noticeRequestDto, principal.name.toLong()))
    }

    @Operation(summary = "공지사항 리스트 조회 API")
    @GetMapping("/api/v1/notice")
    fun selectNotice(noticeSearchDto: NoticeSearchDto, paginateDto: PaginateDto) : ResponseEntity<Page<NoticeListResponseDto>>{
        return ResponseEntity.ok(noticeService.selectNoticePage(noticeSearchDto, paginateDto))
    }


    @Operation(summary = "공지사항 디테일 조회 API")
    @GetMapping("/api/v1/notice/{id}")
    fun detail(
        @Parameter(name = "id", required = false, example = "5",description = "공지사항 ID")
        @PathVariable id : Long) : ResponseEntity<NoticeDetailResponseDto> {
        return ResponseEntity.ok(noticeService.detail(id))
    }

    @Operation(summary = "공지사항 수정 API")
//    @com.ilogistic.delivery_admin_backend.aop.AdminRights
    @PutMapping("/api/v1/notice/{id}")
    fun modify(@Parameter(name = "id", required = false, example = "5",description = "공지사항 ID")
               @PathVariable id : Long, @RequestBody noticeRequestDto: NoticeRequestDto, principal: Principal) : ResponseEntity<String>{
        noticeService.modify(id, noticeRequestDto, principal.name.toLong())
        return ResponseEntity.ok("수정이 완료되었습니다.")
    }

    @Operation(summary = "공지사항 삭제 API")
//    @com.ilogistic.delivery_admin_backend.aop.AdminRights
    @DeleteMapping("/api/v1/notice/{ids}")
    fun delete(@PathVariable ids : List<Long>) : ResponseEntity<String>{
        noticeService.delete(ids)
        return ResponseEntity.ok("삭제가 완료되었습니다.")
    }

    @Operation(summary = "공지사항 조회수증가 API")
    @PostMapping("/api/v1/notice/{id}/hit")
    fun hit(@Parameter(name = "id", required = false, example = "5",description = "공지사항 ID")
            @PathVariable id : Long) : ResponseEntity<String>{
        noticeService.hit(id)
        return ResponseEntity.ok("조회수가 증가되었습니다.")
    }

}
