package com.dn0ne.moneymateserver.utils

import io.jsonwebtoken.Claims
import io.jsonwebtoken.ExpiredJwtException
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.SignatureException
import org.springframework.security.access.AccessDeniedException
import org.springframework.security.core.userdetails.UserDetails
import java.time.Instant
import java.time.temporal.ChronoUnit
import java.util.*

object JwtHelper {
    private const val DAYS = 7L
    private val secretKey = Jwts.SIG.HS256.key().build()

    fun generateToken(email: String): String {
        val now = Instant.now()
        return Jwts.builder()
            .subject(email)
            .issuedAt(Date.from(now))
            .expiration(Date.from(now.plus(DAYS, ChronoUnit.DAYS)))
            .signWith(secretKey)
            .compact()
    }

    fun extractUsername(token: String): String {
        return getTokenBody(token).subject
    }

    fun extractTokenFromHeader(authHeader: String): String {
        return authHeader.substring(7)
    }

    fun validateToken(token: String, userDetails: UserDetails): Boolean {
        val username = extractUsername(token)
        return username == userDetails.username && !isTokenExpired(token)
    }

    private fun getTokenBody(token: String): Claims {
        try {
            return Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .payload
        } catch (e: Exception) {
            throw if (e is SignatureException || e is ExpiredJwtException) {
                AccessDeniedException("Access denied: ${e.message}")
            } else Exception("Unknown error: ${e.message}")
        }
    }

    private fun isTokenExpired(token: String): Boolean {
        return getTokenBody(token).expiration.before(Date())
    }
}