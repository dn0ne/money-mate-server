package com.dn0ne.moneymateserver.repositories

import com.dn0ne.moneymateserver.models.user.User
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository

@Repository
interface UserRepository: MongoRepository<User, String> {
    fun findUserByEmail(email: String): User?
}