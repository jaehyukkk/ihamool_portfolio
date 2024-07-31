package com.ilogistic.delivery_admin_backend.notice.repository

import com.ilogistic.delivery_admin_backend.notice.domain.entity.Notice
import com.ilogistic.delivery_admin_backend.user.domain.entity.User
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime

@Repository
interface NoticeRepository : JpaRepository<Notice, Long>, NoticeRepositoryCustom {


    @Transactional
    @Modifying
    @Query("update Notice a set a.title = :title, a.content = :content, a.user = :user, a.updatedDate = :now where a.id = :id ")
    fun modifyNotice(
        id : Long,
        title : String,
        content : String,
        user : User,
        now : LocalDateTime
    )

    @Transactional
    @Modifying
    @Query("update Notice a set a.hit = a.hit + 1 where a.id = :id ")
    fun modifyHit(
        id : Long
    ) : Int
}
