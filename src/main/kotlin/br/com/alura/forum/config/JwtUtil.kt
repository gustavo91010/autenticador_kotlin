package br.com.alura.forum.config

import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import org.springframework.stereotype.Component
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import java.util.Date

@Component
class JwtUtil {

    val expiraion: Long = 6000 // 1 min

    @Value("\${jwt.secret}")
    private lateinit var secret: String // só vai ser carregado quando for utilizado
    fun generateToken(username: String): String? {

        return Jwts.builder()
            .setSubject(username)
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
        return UsernamePasswordAuthenticationToken(username, null, null)

    }


}