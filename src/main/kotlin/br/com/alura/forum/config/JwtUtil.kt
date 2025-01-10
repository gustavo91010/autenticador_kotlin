package br.com.alura.forum.config

import br.com.alura.forum.model.Role
import br.com.alura.forum.service.UsuarioService
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import org.springframework.stereotype.Component
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import java.util.Date

@Component
class JwtUtil(
    private val usuarioService: UsuarioService
) {

    val expiraion: Long = 64000 // 1 min

    @Value("\${jwt.secret}")
    private lateinit var secret: String // só vai ser carregado quando for utilizado
    fun generateToken(username: String, authorities: List<Role>): String? {

        return Jwts.builder()
            .setSubject(username)
            .claim("role", authorities)
            .setExpiration(Date(System.currentTimeMillis() + expiraion))
            .signWith(
                SignatureAlgorithm.HS512,
                secret.toByteArray() // o tipo do algoritimo de hash vai ser o HS512 e minha chave secreta
            ).compact()
    }


    fun isValue(jwt: String?): Boolean {

        return try {
            Jwts.parser().setSigningKey(secret.toByteArray()).parseClaimsJwt(jwt)
            true
        } catch (e: IllegalArgumentException) {
            false
        }
    }

    fun getAuthentication(jwt: String?): Authentication {
        val username = Jwts.parser().setSigningKey(secret.toByteArray()).parseClaimsJwt(jwt).body.subject
        val userlololo = usuarioService.loadUserByUsername(username)
        return UsernamePasswordAuthenticationToken(username, null, userlololo.authorities)

    }


}