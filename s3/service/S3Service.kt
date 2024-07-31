package com.ilogistic.delivery_admin_backend.s3.service

import com.amazonaws.services.s3.AmazonS3
import com.amazonaws.services.s3.model.CannedAccessControlList
import com.amazonaws.services.s3.model.ObjectMetadata
import com.amazonaws.services.s3.model.PutObjectRequest
import com.amazonaws.util.IOUtils
import com.ilogistic.delivery_admin_backend.exception.BaseException
import com.ilogistic.delivery_admin_backend.file.domain.entity.File
import com.ilogistic.delivery_admin_backend.exception.CustomMessageRuntimeException
import com.ilogistic.delivery_admin_backend.exception.ErrorCode
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import java.io.ByteArrayInputStream
import java.util.*

@Service
class S3Service(
    @Value("\${cloud.aws.s3.bucket}") private val bucket: String,
    private val s3Client: AmazonS3,
    @Value("\${cloud.aws.s3.folder}") private val folder: String
) {

    fun s3Upload(file: MultipartFile) : File {
        val filename = folder + "/" + getSaveFileName(file.originalFilename)
        val objectMetadata = ObjectMetadata()
        objectMetadata.contentType = file.contentType
        try {
            file.inputStream.use { inputStream ->
                val bytes = IOUtils.toByteArray(inputStream)
                objectMetadata.contentLength = bytes.size.toLong()
                val byteArrayInputStream = ByteArrayInputStream(bytes)
                s3Client.putObject(
                    PutObjectRequest(bucket, filename, byteArrayInputStream, objectMetadata)
                        .withCannedAcl(CannedAccessControlList.PublicRead)
                )
            }
        } catch (e: Exception) {
            e.printStackTrace()
            throw BaseException(ErrorCode.INTERNAL_SERVER_ERROR)
        }

        return File(
            originalFilename = file.originalFilename,
            path = s3Client.getUrl(bucket, filename).toString(),
            filename = filename
        )
    }

    private fun getSaveFileName(originalFilename: String?): String {
        val extPosIndex: Int? = originalFilename?.lastIndexOf(".")
        val ext = originalFilename?.substring(extPosIndex?.plus(1) as Int)
        return UUID.randomUUID().toString() + "." + ext
    }
}
