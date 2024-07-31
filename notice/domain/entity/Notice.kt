package com.ilogistic.delivery_admin_backend.notice.domain.entity

import com.ilogistic.delivery_admin_backend.user.domain.entity.User
import org.hibernate.annotations.SQLDelete
import org.hibernate.annotations.Where
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.lang.Boolean
import java.time.LocalDateTime
import javax.persistence.*
import kotlin.Int
import kotlin.Long
import kotlin.String

@Entity
@EntityListeners(AuditingEntityListener::class)
@SQLDelete(sql = "UPDATE notice SET deleted = true WHERE id = ?")
@Where(clause = "deleted = false")
class Notice(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,
    val title : String = "",
    @Column(columnDefinition = "TEXT")
    val content : String = "",
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    var user: User,
    val hit: Int? = 0,
    @CreatedDate
    var createdDate: LocalDateTime? = null,
    @LastModifiedDate
    var updatedDate: LocalDateTime? = null,
) {
    val deleted = Boolean.FALSE
}
