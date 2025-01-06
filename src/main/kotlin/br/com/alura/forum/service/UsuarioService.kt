package br.com.alura.forum.service

import br.com.alura.forum.config.UserDetail
import br.com.alura.forum.exception.NotFoundException
import br.com.alura.forum.model.Usuario
import br.com.alura.forum.repository.UsuarioRepository
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.stereotype.Service

@Service
class UsuarioService(private val repository: UsuarioRepository) : UserDetailsService {

    fun buscarPorId(id: Long) = repository.getOne(id)


    fun findByEmail(email: String): Usuario =
        repository.findByEmail(email)
            .orElseThrow { NotFoundException("Usuário não encontrado!") }


    override fun loadUserByUsername(email: String) = UserDetail(findByEmail(email))


}
