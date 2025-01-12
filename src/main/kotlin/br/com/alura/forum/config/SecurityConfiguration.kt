package br.com.alura.forum.config

import br.com.alura.forum.security.jwt.JWTAuthenticationFilter
import br.com.alura.forum.security.jwt.JWTLoginFilter
import br.com.alura.forum.security.jwt.JwtUtil


import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import org.springframework.security.authentication.AuthenticationManager

@Configuration
@EnableWebSecurity
class SecurityConfiguration(

    private val userDetailService: UserDetailsService,
    private val jwtUtil: JwtUtil
) : WebSecurityConfigurerAdapter() {

    override fun configure(http: HttpSecurity?) {
        // se o http não for nulo...
        http?.csrf()?.disable()
            ?.authorizeRequests()
            ?.antMatchers("/topicos")
            ?.hasAuthority("LEITURA_ESCRITA")
            ?.antMatchers(HttpMethod.POST, "/auth")?.permitAll()// liberando o path logun do verbo POST
            ?.anyRequest() // qualquer requisição deve estar autenticada

            ?.authenticated()?.and()

                    http?.addFilterBefore(
    JWTLoginFilter(authManager = authenticationManager(), jwtUtil = jwtUtil),
    UsernamePasswordAuthenticationFilter::class.java
)

http?.addFilterBefore(
    JWTAuthenticationFilter(jwtUtil = jwtUtil),
    UsernamePasswordAuthenticationFilter::class.java
)
        http
            ?.sessionManagement()
            ?.sessionCreationPolicy(SessionCreationPolicy.STATELESS) // não vou guardar estados dessa autenticação

    }
    
//    @Bean
 private fun byCryptPasswordEncoder(): BCryptPasswordEncoder {
     return BCryptPasswordEncoder()
 }
    override fun configure(auth: AuthenticationManagerBuilder?) {
        auth?.userDetailsService(userDetailService)?.passwordEncoder(byCryptPasswordEncoder())
    }
    @Bean
    override fun authenticationManagerBean(): AuthenticationManager {
        return super.authenticationManagerBean()
    }
    
}