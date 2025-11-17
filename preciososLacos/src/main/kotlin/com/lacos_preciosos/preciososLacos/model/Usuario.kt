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
import java.util.Base64

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
    var email: String = "",

    @Column(name = "password")
    var senha: String? = "",
    var cpf: String = "",
    var telefone: String = "",

    var role: String = "",

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    var data_cadastro: LocalDate = LocalDate.now(),

    @Lob
    @Column(name = "foto_perfil", columnDefinition = "LONGBLOB")
    var fotoPerfil: ByteArray? = null

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

    // Método para adicionar/atualizar foto de perfil
    fun adicionarFotoPerfil(imagemBase64: String) {
        this.fotoPerfil = Base64.getDecoder().decode(imagemBase64)
    }

    // Método para obter a foto de perfil como Base64
    fun getFotoPerfilBase64(): String? {
        return if (fotoPerfil != null) Base64.getEncoder().encodeToString(fotoPerfil) else null
    }

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

    // Sobrescrever equals e hashCode para ignorar o campo fotoPerfil na comparação
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Usuario

        if (idUsuario != other.idUsuario) return false
        if (nomeCompleto != other.nomeCompleto) return false
        if (login != other.login) return false
        if (senha != other.senha) return false
        if (cpf != other.cpf) return false
        if (telefone != other.telefone) return false
        if (role != other.role) return false
        if (data_cadastro != other.data_cadastro) return false

        return true
    }

    override fun hashCode(): Int {
        var result = idUsuario ?: 0
        result = 31 * result + nomeCompleto.hashCode()
        result = 31 * result + login.hashCode()
        result = 31 * result + (senha?.hashCode() ?: 0)
        result = 31 * result + cpf.hashCode()
        result = 31 * result + telefone.hashCode()
        result = 31 * result + role.hashCode()
        result = 31 * result + data_cadastro.hashCode()
        return result
    }
}
