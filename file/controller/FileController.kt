package com.ilogistic.delivery_admin_backend.file.controller

import com.ilogistic.delivery_admin_backend.file.domain.entity.File
import com.ilogistic.delivery_admin_backend.file.service.FileService
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile

@RequestMapping("/api/v1/file")
@RestController
class FileController(
    private val fileService: FileService
) {
    @PostMapping(consumes = [MediaType.MULTIPART_FORM_DATA_VALUE])
    fun create(
        @RequestPart(value = "file") file: MultipartFile
    ) : ResponseEntity<File>{
        return ResponseEntity.ok(fileService.create(file))
    }

    @PostMapping("/qq")
    fun create() : ResponseEntity<String>{
        return ResponseEntity.ok("diosjdoijasoidjaoisd")
    }
}
