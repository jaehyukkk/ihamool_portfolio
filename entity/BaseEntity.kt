package com.ilogistic.delivery_admin_backend.entity

import com.ilogistic.delivery_admin_backend.annotation.AllOpen
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.time.LocalDateTime
import javax.persistence.Column
import javax.persistence.EntityListeners
import javax.persistence.MappedSuperclass

@com.ilogistic.delivery_admin_backend.annotation.AllOpen
@MappedSuperclass
@EntityListeners(AuditingEntityListener::class)
abstract class BaseEntity{
    @CreatedDate
    @Column(nullable = false, updatable = false)
    var createdDate: LocalDateTime = LocalDateTime.MIN

    @LastModifiedDate
    @Column(nullable = false)
    var updatedDate: LocalDateTime = LocalDateTime.MIN
}
