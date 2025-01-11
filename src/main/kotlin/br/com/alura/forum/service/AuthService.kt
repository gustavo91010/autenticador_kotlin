package br.com.alura.forum.service

import br.com.alura.forum.controller.LoginRequest
import br.com.alura.forum.security.JwtUtil
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service
import org.springframework.web.bind.annotation.RequestBody

@Service
class AuthService(
    private val userService: UsuarioService,
    private val jwtUtil: JwtUtil
) {

    fun authenticate(loginRequest: LoginRequest): String? {
        val user = userService.findByEmail(loginRequest.email)
        return jwtUtil.generateToken(user.email, user.roles)
    }
}