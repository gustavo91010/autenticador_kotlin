package br.com.alura.forum.security.jwt

import br.com.alura.forum.model.Role
import br.com.alura.forum.security.Credentials
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import javax.servlet.FilterChain
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

class JWTLoginFilter(

    /*
    Esse classe/ filtro é responsável por interceptar a requisição de login,
     validar as credenciais do usuário e, se forem válidas,
      chamar o método generateToken para criar o JWT
     */
    private val authManager: AuthenticationManager,
    private val jwtUtil: JwtUtil
) :
    UsernamePasswordAuthenticationFilter() {

    override fun attemptAuthentication(request: HttpServletRequest, response: HttpServletResponse): Authentication {
        val credentials = ObjectMapper().readValue(request.inputStream, Credentials::class.java)
        return authManager.authenticate(
            UsernamePasswordAuthenticationToken(credentials.username, credentials.password))

    }

    override fun successfulAuthentication(
        request: HttpServletRequest?,
        response: HttpServletResponse?,
        chain: FilterChain?,
        authResult: Authentication?
    ) {
        val username = authResult ?: throw IllegalStateException("Usuário inválido")

        val authorities = authResult.authorities?.map {
            Role(
                nome = it.authority,
                id = 1
            )
        } ?: emptyList()
        val token = jwtUtil.generateToken(username.principal.toString(), authorities)
        response?.addHeader("Authorization", "Bearer $token")
    }
}

