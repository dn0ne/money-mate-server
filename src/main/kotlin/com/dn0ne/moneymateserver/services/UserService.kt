package com.dn0ne.moneymateserver.services

import com.dn0ne.moneymateserver.models.user.User
import com.dn0ne.moneymateserver.repositories.UserRepository
import com.dn0ne.moneymateserver.services.exceptions.DuplicateException
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class UserService(
    @Autowired private val repository: UserRepository,
    @Autowired private val passwordEncoder: PasswordEncoder
) {

    @Transactional
    fun signup(user: User) {
        val existingUser = repository.findUserByEmail(user.email)
        if (existingUser != null) {
            throw DuplicateException("User with the email address ${user.email} already exists.")
        }

        val hashedPassword = passwordEncoder.encode(user.password)
        val newUser = User(email = user.email, password = hashedPassword)
        repository.insert(newUser)
    }
}

