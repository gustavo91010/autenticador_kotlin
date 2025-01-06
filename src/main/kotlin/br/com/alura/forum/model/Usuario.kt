package br.com.alura.forum.model

import com.fasterxml.jackson.annotation.JsonIgnore
import javax.persistence.*

@Entity
data class Usuario(

        @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
        val id: Long? = null,
        val nome: String,
        val email: String,
        val password: String,

        @JsonIgnore
        @ManyToMany(fetch = FetchType.EAGER) // esse é o NÂO preguisoco, que tarz todos os dados sempre
        @JoinTable(
                name = "usuario_role", // Nome correto da tabela intermediária
                joinColumns = [JoinColumn(name = "usuario_id")], // FK para Usuario
                inverseJoinColumns = [JoinColumn(name = "role_id")] // FK para Role
        )        val roles: List<Role> = mutableListOf()
)
