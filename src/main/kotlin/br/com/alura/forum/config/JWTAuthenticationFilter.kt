package br.com.alura.forum.config

import org.springframework.security.core.context.SecurityContext
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.filter.OncePerRequestFilter
import javax.servlet.FilterChain
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

class JWTAuthenticationFilter(private val jwtUtil: JwtUtil) : OncePerRequestFilter() {
    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        // filtragem das requisições

        val token = request.getHeader("Authorization")
        val jwt = getTpkenDetails(token)
        if (jwtUtil.isValue(jwt)) {
            val authentication = jwtUtil.getAuthentication(jwt)
            SecurityContextHolder.getContext().authentication = authentication
        }
        filterChain.doFilter(request, response)

    }

    private fun getTpkenDetails(token: String?) = token?.let { jwt ->
        jwt.startsWith("Bearer ")// verifica se começa com o bearer
        jwt.substring(7, jwt.length)
    }
}


