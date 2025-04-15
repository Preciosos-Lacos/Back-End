package com.lacos_preciosos.preciososLacos.service

import com.lacos_preciosos.preciososLacos.dto.UsuarioDTO
import com.lacos_preciosos.preciososLacos.model.Usuario
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service

@Service
class UsuarioService {

    fun criarUsuario(usuarioDTO: UsuarioDTO): Usuario {
        return Usuario(usuarioDTO)
    }

    fun listarUsuarios(listaUsuario: List<Usuario>): ResponseEntity<List<Usuario>> {
        return ResponseEntity.status(200).body(listaUsuario);
    }
}