package com.ilogistic.delivery_admin_backend.user.domain.entity

import com.fasterxml.jackson.annotation.JsonIgnore
import com.ilogistic.delivery_admin_backend.admingroup.domain.entity.AdminGroup
import org.hibernate.annotations.SQLDelete
import org.hibernate.annotations.Where
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import java.time.LocalDateTime
import javax.persistence.*

//@JsonIgnoreProperties("answers")
@EntityListeners(AuditingEntityListener::class)
@Entity
@SQLDelete(sql = "UPDATE user SET deleted = true WHERE id = ?")
@Where(clause = "deleted = false")
class User(
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        var id: Long? = null,

        @Column(unique = true)
        private var username: String,

        private var password: String,

        var userRole: String,

        var depositName: String? = null,

        @JsonIgnore
        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "admin_group_id")
        val adminGroup: AdminGroup? = null,

        @CreatedDate
        var createdDate: LocalDateTime? = null,

        @LastModifiedDate
        var updatedDate: LocalDateTime? = null,

        val deleted: Boolean? = false


        ) : UserDetails {


//        @JsonIgnore
//        @OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = [CascadeType.MERGE], orphanRemoval = true)
//        val answers: List<Answer>? = null

        override fun getAuthorities(): Collection<GrantedAuthority> {
                val roles: MutableSet<GrantedAuthority> = HashSet()
                for (role in userRole.split(",".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()) {
                        roles.add(SimpleGrantedAuthority(role))
                }
                return roles
        }

        override fun getUsername(): String {
                return username
        }

        // 사용자의 password를 반환
        override fun getPassword(): String {
                return password
        }


        // 계정 만료 여부 반환
        override fun isAccountNonExpired(): Boolean {
                // 만료되었는지 확인하는 로직
                return true // true -> 만료되지 않았음
        }

        // 계정 잠금 여부 반환
        override fun isAccountNonLocked(): Boolean {
                // 계정 잠금되었는지 확인하는 로직
                return true // true -> 잠금되지 않았음
        }

        // 패스워드의 만료 여부 반환
        override fun isCredentialsNonExpired(): Boolean {
                // 패스워드가 만료되었는지 확인하는 로직
                return true // true -> 만료되지 않았음
        }

        // 계정 사용 가능 여부 반환
        override fun isEnabled(): Boolean {
                // 계정이 사용 가능한지 확인하는 로직
                return true // true -> 사용 가능
        }
}
