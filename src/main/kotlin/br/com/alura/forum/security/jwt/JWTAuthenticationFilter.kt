package br.com.alura.forum.security.jwt

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
        val jwt = getTokenDetails(token)

        if (jwtUtil.isValue(jwt)) {
            val authentication = jwtUtil.getAuthentication(jwt)
            SecurityContextHolder.getContext().authentication = authentication
        }
        filterChain.doFilter(request, response)

    }

    private fun getTokenDetails(token: String?): String? {

        return token?.let { jwt ->
            if (jwt.startsWith("Bearer ")) {
                jwt.substring(7)
            } else {
                null
            }
        }
    }
}





