package com.lacos_preciosos.preciososLacos.service

import com.lacos_preciosos.preciososLacos.dto.usuario.CadastroUsuarioDTO
import com.lacos_preciosos.preciososLacos.model.Usuario
import com.lacos_preciosos.preciososLacos.repository.UsuarioRepository
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service

@Service
class UsuarioService(private val usuarioRepository: UsuarioRepository) {

    fun criarUsuario(cadastroUsuarioDTO: CadastroUsuarioDTO): Usuario {
        return Usuario(cadastroUsuarioDTO)
    }

    fun listarUsuarios(listaUsuario: List<Usuario>): ResponseEntity<List<Usuario>> {
        return ResponseEntity.status(200).body(listaUsuario);
    }
}