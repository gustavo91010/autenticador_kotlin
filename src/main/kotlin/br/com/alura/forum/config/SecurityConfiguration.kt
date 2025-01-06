package br.com.alura.forum.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.builders.WebSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder

@Configuration
@EnableWebSecurity
class SecurityConfiguration(private val userDetailService: UserDetailsService) : WebSecurityConfigurerAdapter() {

    override fun configure(http: HttpSecurity?) {
        // se o http não for nulo...
        http?.authorizeRequests()
            ?.antMatchers("/topicos")//indicando para verificar na role do usuário
            ?.hasAuthority("LEITURA_ESCRITA")
            ?.anyRequest() // qualquer requisição deve estar autenticada
            ?.authenticated()?.and()
            ?.sessionManagement()
            ?.sessionCreationPolicy(SessionCreationPolicy.STATELESS) // não vou guardar estados dessa autenticação
            ?.and()
            ?.formLogin()?.disable() // desabilita a autenticação para o login
            ?.httpBasic() // habilita um login basico

    }

    override fun configure(auth: AuthenticationManagerBuilder?) {
        auth?.userDetailsService(userDetailService)?.passwordEncoder(byCryptPasswordEncoder())
    }

//    @Bean
    private fun byCryptPasswordEncoder(): BCryptPasswordEncoder {
        return BCryptPasswordEncoder()
    }
}