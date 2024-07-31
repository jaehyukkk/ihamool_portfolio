package com.ilogistic.delivery_admin_backend.admingroup.service

import com.ilogistic.delivery_admin_backend.admingroup.domain.dto.AdminGroupRequestDto
import com.ilogistic.delivery_admin_backend.admingroup.domain.dto.AdminGroupResponseDto
import com.ilogistic.delivery_admin_backend.admingroup.domain.dto.AdminGroupUserListResponseDto
import com.ilogistic.delivery_admin_backend.admingroup.domain.dto.AdminGroupUserSearchDto
import com.ilogistic.delivery_admin_backend.user.domain.dto.UserListResponseDto
import com.ilogistic.delivery_admin_backend.user.domain.dto.UserSearchDto
import com.ilogistic.delivery_admin_backend.admingroup.domain.entity.AdminGroup
import com.ilogistic.delivery_admin_backend.admingroup.domain.repository.AdminGroupRepository
import com.ilogistic.delivery_admin_backend.dto.common.PaginateDto
import com.ilogistic.delivery_admin_backend.exception.BaseException
import com.ilogistic.delivery_admin_backend.exception.ErrorCode
import com.ilogistic.delivery_admin_backend.user.repository.user.UserRepository
import org.springframework.data.domain.Page
import org.springframework.stereotype.Service
import javax.transaction.Transactional

@Service
class AdminGroupService(
    private val adminGroupRepository: AdminGroupRepository,
    private val userRepository: UserRepository
) {

    fun create(adminGroupRequestDto: AdminGroupRequestDto) : AdminGroup{
        if(adminGroupRepository.existsByCompanyNumber(adminGroupRequestDto.companyNumber)){
            throw BaseException(ErrorCode.DUPLICATE_COMPANY_NUMBER)
        }
        return adminGroupRepository.save(adminGroupRequestDto.toEntity())
    }

    fun modify(id: Long, adminGroupRequestDto: AdminGroupRequestDto){
        if(adminGroupRepository.modify(id, adminGroupRequestDto) == 0){
            throw BaseException(ErrorCode.INTERNAL_SERVER_ERROR)
        }
    }

    fun list() : List<AdminGroupResponseDto>{
        return adminGroupRepository.getAdminGroupList()
    }

    fun detail(id: Long) : AdminGroupResponseDto{
        return adminGroupRepository.getAdminGroupDetail(id) ?: throw BaseException(ErrorCode.ENTITY_NOT_FOUND)
    }

    fun entity(id: Long) : AdminGroup{
        return adminGroupRepository.findById(id).orElseThrow { BaseException(ErrorCode.ENTITY_NOT_FOUND) }
    }

    fun addUsers(adminGroupId: Long, userIds: List<Long>) {
        val adminGroup = adminGroupRepository.findById(adminGroupId).orElseThrow { BaseException(ErrorCode.ENTITY_NOT_FOUND) }
        userRepository.addUsersToAdminGroup(adminGroup, userIds)
    }

    fun addUser(adminGroupId: Long, userId: Long) {
        val adminGroup = adminGroupRepository.findById(adminGroupId).orElseThrow { BaseException(ErrorCode.ENTITY_NOT_FOUND) }
        userRepository.addUserToAdminGroup(adminGroup, userId)
    }

    fun getUsers(
        adminGroupId: Long,
        paginateDto: PaginateDto,
        adminGroupUserSearchDto: AdminGroupUserSearchDto
    ): Page<AdminGroupUserListResponseDto> {
        return userRepository.getUsersByAdminGroup(adminGroupId, paginateDto.pageable(), adminGroupUserSearchDto)
    }

    fun delete(id: Long){
        adminGroupRepository.deleteById(id)
    }

    @Transactional
    fun deleteUser(adminGroupId: Long, userId: Long) {
        val adminGroup = adminGroupRepository.findById(adminGroupId).orElseThrow { BaseException(ErrorCode.ENTITY_NOT_FOUND) }
        if(userRepository.deleteUserFromAdminGroup(adminGroup, userId) == 0L){
            throw BaseException(ErrorCode.ENTITY_NOT_FOUND)
        }
    }

    @Transactional
    fun deleteUsers(adminGroupId: Long, userIds: List<Long>) {
        val adminGroup = adminGroupRepository.findById(adminGroupId).orElseThrow { BaseException(ErrorCode.ENTITY_NOT_FOUND) }
        if(userRepository.deleteUsersFromAdminGroup(adminGroup, userIds) != userIds.size.toLong()){
            throw BaseException(ErrorCode.ENTITY_NOT_FOUND)
        }
    }
}
