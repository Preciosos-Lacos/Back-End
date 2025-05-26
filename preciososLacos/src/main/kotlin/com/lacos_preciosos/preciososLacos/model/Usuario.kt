package com.lacos_preciosos.preciososLacos.model

import com.fasterxml.jackson.annotation.JsonProperty
import com.lacos_preciosos.preciososLacos.dto.usuario.CadastroUsuarioDTO
import jakarta.persistence.*
import java.time.LocalDate
 @Entity
data class Usuario(
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @Id // do pacote jakarta.persistence
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    @Column(name= "id_usuario")
    var idUsuario: Int? = null,

    @Column(name = "nome_completo")
    var nomeCompleto: String,

    var email: String,
    var senha: String,
    var cpf: String,
    var telefone: String,
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    var data_cadastro: LocalDate = LocalDate.now(),
    var autenticado: Boolean = false
) {
     constructor(idUsuario: Int) : this(
         idUsuario = null,
         nomeCompleto = "",
         email = "",
         senha = "",
         cpf = "",
         telefone = "",
         data_cadastro = LocalDate.now()
     )


     constructor(cadastroUsuarioDTO: CadastroUsuarioDTO): this(
        nomeCompleto = cadastroUsuarioDTO.nomeCompleto,
        senha = cadastroUsuarioDTO.senha,
        telefone = cadastroUsuarioDTO.telefone,
        email = cadastroUsuarioDTO.email,
        cpf = cadastroUsuarioDTO.cpf
    )
    override fun toString(): String {
        return "Usuario(nome='$nomeCompleto', email='$email', cpf='$cpf',senha = '$senha', telefone = '$telefone', data_cadastro = $data_cadastro)"
    }

}