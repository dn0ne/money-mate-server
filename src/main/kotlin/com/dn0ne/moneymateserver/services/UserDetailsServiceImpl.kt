package com.dn0ne.moneymateserver.services

import com.dn0ne.moneymateserver.models.user.User
import com.dn0ne.moneymateserver.repositories.UserRepository
import com.dn0ne.moneymateserver.services.exceptions.NotFoundException
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.stereotype.Service

@Service
class UserDetailsServiceImpl(
    @Autowired val repository: UserRepository
): UserDetailsService {
    override fun loadUserByUsername(email: String): UserDetails {
        val user: User = repository.findUserByEmail(email)
            ?: throw NotFoundException("User does not exist, email: $email")

        return org.springframework.security.core.userdetails.User.builder()
            .username(user.email)
            .password(user.password)
            .build()
    }
}