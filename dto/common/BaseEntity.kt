package com.ilogistic.delivery_admin_backend.dto.common

import org.hibernate.annotations.Comment
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.time.LocalDateTime
import javax.persistence.Column
import javax.persistence.EntityListeners
import javax.persistence.MappedSuperclass

@MappedSuperclass
@EntityListeners(AuditingEntityListener::class)
abstract class BaseEntity {
    @CreatedDate
    @Column(nullable = false, updatable = false)
    @Comment("생성일")
    open var createdDate: LocalDateTime = LocalDateTime.MIN


    @LastModifiedDate
    @Column(nullable = true)
    @Comment("수정일")
    open var updatedDate: LocalDateTime = LocalDateTime.MIN
}
