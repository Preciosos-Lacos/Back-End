package com.lacos_preciosos.preciososLacos.model

import com.fasterxml.jackson.annotation.JsonProperty
import com.lacos_preciosos.preciososLacos.dto.UsuarioDTO
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
    var data_cadastro: LocalDate = LocalDate.now()
) {
     constructor() : this(
         idUsuario = null,
         nomeCompleto = "",
         email = "",
         senha = "",
         cpf = "",
         telefone = "",
         data_cadastro = LocalDate.now()
     )


     constructor(usuarioDTO: UsuarioDTO): this(
        nomeCompleto = usuarioDTO.nomeCompleto,
        senha = usuarioDTO.senha,
        telefone = usuarioDTO.telefone,
        email = usuarioDTO.email,
        cpf = usuarioDTO.cpf
    )
    override fun toString(): String {
        return "Usuario(nome='$nomeCompleto', email='$email', cpf='$cpf',senha = '$senha', telefone = '$telefone', data_cadastro = $data_cadastro)"
    }

}