package br.com.alura.forum.controller

import br.com.alura.forum.service.AuthService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/auth")
class AuthController(
    private val auth: AuthService
) {
    @PostMapping()
    fun authenticate(@RequestBody loginRequest: LoginRequest): ResponseEntity<String> {

        val token= auth.authenticate(loginRequest)
        return ResponseEntity.ok(token)
    }

}


