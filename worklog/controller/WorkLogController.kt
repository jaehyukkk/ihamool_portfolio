package com.ilogistic.delivery_admin_backend.worklog.controller

import com.ilogistic.delivery_admin_backend.aop.UserRights
import com.ilogistic.delivery_admin_backend.user.enums.UserRole
import com.ilogistic.delivery_admin_backend.user.service.UserService
import com.ilogistic.delivery_admin_backend.worklog.service.WorkLogService
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.security.Principal

@RestController
@RequestMapping("/api/v1/work-log")
class WorkLogController(
    private val workLogService: WorkLogService,
    private val userService: UserService
) {

    @UserRights([UserRole.DRIVER])
    @PostMapping("/on")
    fun onWork(principal: Principal) {
        workLogService.onWork(userService.getDriverEntity(principal.name.toLong()))
    }

    @UserRights([UserRole.DRIVER])
    @PostMapping("/off")
    fun offWork(principal: Principal) {
        workLogService.offWork(userService.getDriverEntity(principal.name.toLong()))
    }

}
