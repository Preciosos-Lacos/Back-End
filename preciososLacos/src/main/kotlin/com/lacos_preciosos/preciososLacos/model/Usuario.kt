package com.lacos_preciosos.preciososLacos.model

import com.fasterxml.jackson.annotation.JsonProperty
import com.lacos_preciosos.preciososLacos.dto.usuario.CadastroUsuarioDTO
import jakarta.persistence.*
import lombok.Getter
import lombok.Setter
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import java.time.LocalDate

@Entity
data class Usuario(

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @Id // do pacote jakarta.persistence
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    @Column(name = "id_usuario")
    var idUsuario: Int? = null,

    @Column(name = "nome_completo")
    var nomeCompleto: String = "",

    var login: String = "",

    @Column(name = "password")
    var senha: String? = "",
    var cpf: String = "",
    var telefone: String = "",

    var role: String = "",

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    var data_cadastro: LocalDate = LocalDate.now()

) : UserDetails {
    constructor(idUsuario: Int?) : this(
        idUsuario = null,
        nomeCompleto = "",
        login = "",
        senha = "",
        cpf = "",
        telefone = "",
        data_cadastro = LocalDate.now()
    )

    constructor(cadastroUsuarioDTO: CadastroUsuarioDTO) : this(
        nomeCompleto = cadastroUsuarioDTO.nomeCompleto,
        senha =  cadastroUsuarioDTO.senha,
        telefone = cadastroUsuarioDTO.telefone,
        login = cadastroUsuarioDTO.email,
        role = "ROLE_USER",
        cpf = cadastroUsuarioDTO.cpf
    )

    override fun toString(): String {
        return "Usuario(nome='$nomeCompleto', email='$login', cpf='$cpf',senha = '$senha', telefone = '$telefone', data_cadastro = $data_cadastro)"
    }

        override fun getAuthorities(): Collection<GrantedAuthority?>? {
            if (this.role == "ROLE_USER") {
                return listOf(SimpleGrantedAuthority("ROLE_USER"))
            }
            return listOf(SimpleGrantedAuthority("ROLE_USER"), SimpleGrantedAuthority("ROLE_ADMIN"))
        }

        override fun getPassword(): String? {
            return this.senha;
        }

        override fun getUsername(): String? {
            return this.login;
        }


    }
