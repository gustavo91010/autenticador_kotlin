package br.com.alura.forum.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.builders.WebSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import org.springframework.web.filter.OncePerRequestFilter

@Configuration
@EnableWebSecurity
class SecurityConfiguration(
    private val userDetailService: UserDetailsService,
    private val jwtUtil: JwtUtil
) : WebSecurityConfigurerAdapter() {

    override fun configure(http: HttpSecurity?) {
        // se o http não for nulo...
        http?.authorizeRequests()
            ?.antMatchers(HttpMethod.POST, "/login")?.permitAll()// liberando o path logun do verbo POST
        ?.anyRequest() // qualquer requisição deve estar autenticada
            ?.authenticated()?.and()
        http?.addFilterAfter(
            JWTLoginFilter(authManager = authenticationManager(), jwtUtil = jwtUtil),
            UsernamePasswordAuthenticationFilter().javaClass
        )
        http?.addFilterBefore(JWTAuthenticationFilter(jwtUtil= jwtUtil), OncePerRequestFilter::class.java)// fazer a validação do token a cada requisição
        http
            ?.sessionManagement()
            ?.sessionCreationPolicy(SessionCreationPolicy.STATELESS) // não vou guardar estados dessa autenticação

    }

    override fun configure(auth: AuthenticationManagerBuilder?) {
        auth?.userDetailsService(userDetailService)?.passwordEncoder(byCryptPasswordEncoder())
    }

    //    @Bean
    private fun byCryptPasswordEncoder(): BCryptPasswordEncoder {
        return BCryptPasswordEncoder()
    }
}