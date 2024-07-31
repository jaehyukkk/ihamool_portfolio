package com.ilogistic.delivery_admin_backend.user.repository.user

import com.ilogistic.delivery_admin_backend.user.domain.entity.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Repository
interface UserRepository : JpaRepository<User, Long>, UserRepositoryCustom {
    fun findByUsername(email: String): User?
    fun existsByUsername(username : String): Boolean

    @Transactional
    @Modifying
    @Query("update User a set a.password = :password where a.id = :id ")
    fun modifyPassword(
        id : Long,
        password : String
    ) : Int
}
