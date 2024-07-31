package com.ilogistic.delivery_admin_backend.utils

import org.springframework.web.multipart.MultipartFile
import java.io.File
import java.util.*

class FileUtil {

    companion object{
        fun saveFile(file : MultipartFile, filePath : String) : com.ilogistic.delivery_admin_backend.file.domain.entity.File {

            // 원본파일이름, 저장될 파일이름
            val originalFilename: String? = file.originalFilename
            val saveFileName = getSaveFileName(originalFilename)

            //경로에 폴더가 없으면 폴더 생성
            if (!File(filePath).exists()) {
                try {
                    File(filePath).mkdir()
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }

            if (originalFilename != null) {
                file.transferTo(File(filePath + saveFileName))
            }

            return com.ilogistic.delivery_admin_backend.file.domain.entity.File(
                originalFilename = originalFilename,
                path = filePath + saveFileName,
                filename = saveFileName
            )
        }

        private fun getSaveFileName(originalFilename: String?): String {
            val extPosIndex: Int? = originalFilename?.lastIndexOf(".")
            val ext = originalFilename?.substring(extPosIndex?.plus(1) as Int)
            return UUID.randomUUID().toString() + "." + ext
        }
    }
}
