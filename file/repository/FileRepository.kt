package com.ilogistic.delivery_admin_backend.file.repository

import com.ilogistic.delivery_admin_backend.file.domain.entity.File
import org.springframework.data.jpa.repository.JpaRepository

interface FileRepository : JpaRepository<File, Long> {

}
