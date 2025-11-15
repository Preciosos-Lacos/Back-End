package com.lacos_preciosos.preciososLacos.service

import com.lacos_preciosos.preciososLacos.dto.usuario.AtualizarSenhaDTO
import com.lacos_preciosos.preciososLacos.dto.usuario.CadastroUsuarioDTO
import com.lacos_preciosos.preciososLacos.model.Usuario
import com.lacos_preciosos.preciososLacos.repository.UsuarioRepository
import com.lacos_preciosos.preciososLacos.validacao.ValidacaoException
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service

@Service
class UsuarioService(
    private val usuarioRepository: UsuarioRepository,
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

        return usuarioRepository.atualizarSenha(
            atualizarSenhaDTO.email,
            passwordEncoder.encode(atualizarSenhaDTO.senha)
        )
    }

    fun updateDados(
        nome: String?, telefone: String?, cpf: String?,
        email: String?, senha: String?, login: String
    ): Int {

        var updates = 0

        if (nome?.isNotBlank() == true)
            updates += usuarioRepository.updateNome(nome, login)

        if (telefone?.isNotBlank() == true)
            updates += usuarioRepository.updateTelefone(telefone, login)

        if (cpf?.isNotBlank() == true)
            updates += usuarioRepository.updateCpf(cpf, login)

        if (email?.isNotBlank() == true)
            updates += usuarioRepository.updateEmail(email, login)

        if (senha?.isNotBlank() == true)
            updates += usuarioRepository.updateSenha(passwordEncoder.encode(senha), login)

        return updates
    }
}