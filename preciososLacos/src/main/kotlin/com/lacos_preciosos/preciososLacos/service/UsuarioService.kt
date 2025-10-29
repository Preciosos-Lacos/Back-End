package com.lacos_preciosos.preciososLacos.service

import com.lacos_preciosos.preciososLacos.dto.usuario.AtualizarSenhaDTO
import com.lacos_preciosos.preciososLacos.dto.usuario.CadastroUsuarioDTO
import com.lacos_preciosos.preciososLacos.model.Usuario
import com.lacos_preciosos.preciososLacos.repository.UsuarioRepository
import com.lacos_preciosos.preciososLacos.validacao.ValidacaoException
import org.springframework.http.ResponseEntity
import org.springframework.security.crypto.bcrypt.BCrypt
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import java.util.Optional

@Service
class UsuarioService(private val usuarioRepository: UsuarioRepository,
                     private val passwordEncoder: PasswordEncoder,
) {

    fun criarUsuario(cadastroUsuarioDTO: CadastroUsuarioDTO): Usuario {
        return Usuario(cadastroUsuarioDTO)
    }

    fun listarUsuarios(): List<Usuario> {
        return usuarioRepository.findAll();
    }

    fun atualizarSenha(atualizarSenhaDTO: AtualizarSenhaDTO): Int {
        val usuario = usuarioRepository.findByEmail(atualizarSenhaDTO.email)

        if (usuario == null) {
            throw ValidacaoException("Usuário não encontrado")
        }

        if (usuario.senha == atualizarSenhaDTO.senha) {
            throw ValidacaoException("Senhas são idênticas")
        }

        return usuarioRepository.atualizarSenha(atualizarSenhaDTO.email,passwordEncoder.encode(atualizarSenhaDTO.senha))

    }


}