package com.ilogistic.delivery_admin_backend.admingroup.controller

import com.ilogistic.delivery_admin_backend.admingroup.domain.dto.AdminGroupRequestDto
import com.ilogistic.delivery_admin_backend.admingroup.domain.dto.AdminGroupResponseDto
import com.ilogistic.delivery_admin_backend.admingroup.domain.dto.AdminGroupUserListResponseDto
import com.ilogistic.delivery_admin_backend.admingroup.domain.dto.AdminGroupUserSearchDto
import com.ilogistic.delivery_admin_backend.user.domain.dto.UserListResponseDto
import com.ilogistic.delivery_admin_backend.user.domain.dto.UserSearchDto
import com.ilogistic.delivery_admin_backend.admingroup.domain.entity.AdminGroup
import com.ilogistic.delivery_admin_backend.admingroup.service.AdminGroupService
import com.ilogistic.delivery_admin_backend.aop.UserRights
import com.ilogistic.delivery_admin_backend.dto.common.PaginateDto
import com.ilogistic.delivery_admin_backend.user.enums.UserRole
import io.swagger.v3.oas.annotations.Operation
import org.springframework.data.domain.Page
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RequestMapping("/api/v1/admin-group")
@RestController
class AdminGroupController(
    private val adminGroupService: AdminGroupService
) {

    @UserRights([UserRole.SUPER_ADMIN])
    @Operation(summary = "관리자 그룹 생성", description = "관리자 그룹을 생성하는 API")
    @PostMapping()
    fun create(
        @RequestBody adminGroupRequestDto: AdminGroupRequestDto
    ) : ResponseEntity<AdminGroup>{
        return ResponseEntity.ok(adminGroupService.create(adminGroupRequestDto))
    }

    @UserRights([UserRole.SUPER_ADMIN])
    @Operation(summary = "관리자 그룹 수정", description = "관리자 그룹을 수정하는 API")
    @PutMapping("/{id}")
    fun modify(
        @PathVariable id: Long,
        @RequestBody adminGroupRequestDto: AdminGroupRequestDto
    ) : ResponseEntity<Void>{
        adminGroupService.modify(id, adminGroupRequestDto)
        return ResponseEntity(HttpStatus.OK)
    }

    @UserRights([UserRole.SUPER_ADMIN])
    @Operation(summary = "관리자 그룹 리스트", description = "관리자 그룹 리스트를 조회하는 API")
    @GetMapping()
    fun list() : ResponseEntity<List<AdminGroupResponseDto>>{
        return ResponseEntity.ok(adminGroupService.list())
    }

    @UserRights([UserRole.SUPER_ADMIN])
    @Operation(summary = "관리자 그룹 상세", description = "관리자 그룹 상세를 조회하는 API")
    @GetMapping("/{id}")
    fun detail(
        @PathVariable id: Long
    ) : ResponseEntity<AdminGroupResponseDto>{
        return ResponseEntity.ok(adminGroupService.detail(id))
    }

    @UserRights([UserRole.SUPER_ADMIN])
    @Operation(summary = "관리자 그룹에 사용자 추가 (배열)", description = "관리자 그룹에 사용자를 여러명 추가하는 API")
    @PatchMapping("/{id}/users/{userIds}")
    fun addUsers(
        @PathVariable id: Long,
        @PathVariable userIds: List<Long>
    ) : ResponseEntity<Void>{
        adminGroupService.addUsers(id, userIds)
        return ResponseEntity(HttpStatus.OK)
    }

    @UserRights([UserRole.SUPER_ADMIN])
    @Operation(summary = "관리자 그룹에 사용자 추가", description = "관리자 그룹에 사용자를 추가하는 API")
    @PatchMapping("/{id}/user/{userId}")
    fun addUser(
        @PathVariable id: Long,
        @PathVariable userId: Long
    ) : ResponseEntity<Void>{
        adminGroupService.addUser(id, userId)
        return ResponseEntity(HttpStatus.OK)
    }

    @UserRights([UserRole.SUPER_ADMIN])
    @Operation(summary = "관리자 그룹 소속 회원리스트", description = "관리자 그룹 소속 회원리스트 API")
    @GetMapping("/{id}/user")
    fun getUsers(
        @PathVariable id: Long,
        paginateDto: PaginateDto,
        adminGroupUserSearchDto: AdminGroupUserSearchDto
    ) : ResponseEntity<Page<AdminGroupUserListResponseDto>> {

        return ResponseEntity.ok(adminGroupService.getUsers(id, paginateDto, adminGroupUserSearchDto))
    }

    @UserRights([UserRole.SUPER_ADMIN])
    @Operation(summary = "관리자 그룹 삭제", description = "관리자 그룹을 삭제하는 API")
    @DeleteMapping("/{id}")
    fun delete(
        @PathVariable id: Long
    ) : ResponseEntity<Void>{
        adminGroupService.delete(id)
        return ResponseEntity(HttpStatus.OK)
    }

    @UserRights([UserRole.SUPER_ADMIN])
    @Operation(summary = "관리자 그룹에서 사용자 삭제", description = "관리자 그룹에서 사용자를 삭제하는 API")
    @DeleteMapping("/{id}/user/{userIds}")
    fun deleteUser(
        @PathVariable id: Long,
        @PathVariable userIds: List<Long>
    ) : ResponseEntity<Void>{
        adminGroupService.deleteUsers(id, userIds)
        return ResponseEntity(HttpStatus.OK)
    }
}
