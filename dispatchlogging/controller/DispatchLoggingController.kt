package com.ilogistic.delivery_admin_backend.dispatchlogging.controller

import com.ilogistic.delivery_admin_backend.dispatchlogging.service.DispatchLoggingService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RequestMapping("/api/v1/dispatch-logging")
@RestController
class DispatchLoggingController(
    private val dispatchLoggingService: DispatchLoggingService
) {

    @GetMapping("/{id}")
    fun getLoggingList(@PathVariable id: Long) = ResponseEntity.ok(dispatchLoggingService.getLoggingList(id))
}
