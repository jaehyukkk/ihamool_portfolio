package com.ilogistic.delivery_admin_backend.file.service

import com.ilogistic.delivery_admin_backend.file.domain.entity.File
import com.ilogistic.delivery_admin_backend.file.repository.FileRepository
import com.ilogistic.delivery_admin_backend.s3.service.S3Service
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile

@Service
class FileService(
    private val fileRepository: FileRepository,
    private val s3Service: S3Service
) {

    fun create(file: MultipartFile) : File{
        return fileRepository.save(fileRepository.save(s3Service.s3Upload(file)))
    }
}
