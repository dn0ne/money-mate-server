package com.dn0ne.moneymateserver.controllers

import com.dn0ne.moneymateserver.models.user.User
import com.dn0ne.moneymateserver.models.user.UserValidator
import com.dn0ne.moneymateserver.utils.JwtHelper
import com.dn0ne.moneymateserver.services.exceptions.DuplicateException
import com.dn0ne.moneymateserver.services.UserService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/auth")
class AuthenticationController(
    private val userService: UserService,
    private val authenticationManager: AuthenticationManager
) {

    @PostMapping("/signup", consumes = ["application/json"])
    fun signup(@RequestBody user: User): ResponseEntity<String> {

        val errorString = UserValidator.validateUser(user).let { result ->
            listOfNotNull(
                result.emailError,
                result.passwordError
            ).joinToString(";")
        }

        if (errorString.isNotEmpty()) {
            return ResponseEntity.unprocessableEntity().body(errorString)
        }


        try {
            userService.signup(user)
        } catch (e: DuplicateException) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.message)
        }

        return ResponseEntity.status(HttpStatus.CREATED).build()
    }

    @PostMapping("/login", consumes = ["application/json"])
    fun login(@RequestBody user: User): ResponseEntity<String> {
        authenticationManager.authenticate(UsernamePasswordAuthenticationToken(user.email, user.password))
        val token = JwtHelper.generateToken(user.email)
        return ResponseEntity.ok(token)
    }
}