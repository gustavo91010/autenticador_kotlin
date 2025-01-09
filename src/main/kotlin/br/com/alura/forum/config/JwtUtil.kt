package br.com.alura.forum.config

import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.stereotype.Component
import java.util.Date

@Component
class JwtUtil {
    private val expiration: Long = 6000

    @Value("\${jwt.secret}")
    private lateinit var secret: String

    fun generateToken(username: String) =
        Jwts.builder()
            .setSubject(username)
            .setExpiration(Date(System.currentTimeMillis() + expiration))
            .signWith(SignatureAlgorithm.HS512, secret.toByteArray())
            .compact()

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