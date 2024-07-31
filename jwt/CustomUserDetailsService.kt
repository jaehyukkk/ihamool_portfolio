package com.ilogistic.delivery_admin_backend.jwt

import com.ilogistic.delivery_admin_backend.redis.service.RedisService
import com.ilogistic.delivery_admin_backend.user.domain.entity.User
import com.ilogistic.delivery_admin_backend.user.enums.UserRole
import com.ilogistic.delivery_admin_backend.user.repository.user.UserRepository
import com.ilogistic.delivery_admin_backend.user.service.UserService
import lombok.RequiredArgsConstructor
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional


@RequiredArgsConstructor
@Service
class CustomUserDetailsService(
    private val redisService: RedisService,
    private val userService: UserService
) : UserDetailsService {


    @Transactional
    @Throws(UsernameNotFoundException::class)
    override fun loadUserByUsername(username: String): UserDetails {
        return createUserDetails(userService.findByUsername(username)?:
        throw UsernameNotFoundException("$username -> 데이터베이스에서 찾을 수 없습니다."))
    }

    private fun createUserDetails(user: User): UserDetails {
        if (user.userRole == UserRole.DRIVER.value) {
            userService.getDriverRedisSaveDto(user.id!!).let {
                redisService.setObjectValues("USER-${user.id}", it)
            }
        }
        val grantedAuthority: GrantedAuthority = SimpleGrantedAuthority(user.userRole)
        return org.springframework.security.core.userdetails.User(java.lang.String.valueOf(user.id),
                user.password, setOf(grantedAuthority))
    }
}

