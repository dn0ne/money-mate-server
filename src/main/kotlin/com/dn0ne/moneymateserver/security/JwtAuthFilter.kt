package com.dn0ne.moneymateserver.security

import com.dn0ne.moneymateserver.utils.JwtHelper
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.access.AccessDeniedException
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter

@Component
class JwtAuthFilter(
    private val userDetailsService: UserDetailsService): OncePerRequestFilter() {
    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        try {
            val authHeader: String? = request.getHeader("Authorization")
            var token: String? = null
            var username: String? = null

            authHeader?.let {
                if (it.startsWith("Bearer ")) {
                    token = it.substring(7)
                    username = JwtHelper.extractUsername(token!!)
                }
            }

            token?.let { token ->
                username?.let { username ->
                    if (SecurityContextHolder.getContext().authentication == null) {
                        val userDetails = userDetailsService.loadUserByUsername(username)
                        if (JwtHelper.validateToken(token, userDetails)) {
                            val authenticationToken = UsernamePasswordAuthenticationToken(username, null, null)
                            authenticationToken.details = WebAuthenticationDetailsSource().buildDetails(request)
                            SecurityContextHolder.getContext().authentication = authenticationToken
                        }
                    }
                }
            }

            filterChain.doFilter(request, response)
        } catch (e: AccessDeniedException) {
            response.status = HttpServletResponse.SC_FORBIDDEN
            e.message?.let { response.writer.write(it) }
        }
    }
}