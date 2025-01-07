package br.com.alura.forum.config
import br.com.alura.forum.config.JwtUtil
import br.com.alura.forum.security.JWTLoginFilter


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
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter

@Configuration
@EnableWebSecurity
class SecurityConfiguration(
    private val userDetailService: UserDetailsService
    private val jwtUtil: JwtUtil) : WebSecurityConfigurerAdapter() {

    override fun configure(http: HttpSecurity?) {
        // se o http não for nulo...
        http?.authorizeRequests()
           // ?.antMatchers("/topicos")//indicando para verificar na role do usuário
            // ?.hasAuthority("LEITURA_ESCRITA")
            ?.antMatchers("/login")?.permitAll()// permitir o login
            ?.anyRequest() // qualquer requisição deve estar autenticada
            ?.authenticated()?.and()
// ele pode para no and e voltar de novo apartir do http é???
            http?.addFilterBefore(JWTLoginFilter(authManager = authenticationManager(), jwtUtil= jwtUtil), UsernamePasswordAuthenticationFilter().javaClass)
            http?.sessionManagement()
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