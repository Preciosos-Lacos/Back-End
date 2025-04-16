package com.lacos_preciosos.preciososLacos.controller

import com.lacos_preciosos.preciososLacos.dto.AutenticacaoUsuarioDTO
import com.lacos_preciosos.preciososLacos.model.Usuario
import com.lacos_preciosos.preciososLacos.repository.UsuarioRepository
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/usuarios")
class UsuarioController(private val repositorio: UsuarioRepository) {

    // val usuariosList = mutableListOf<Usuario>();

    /*  @PostMapping
    fun criarUsuario(@RequestBody usuarioDTO: UsuarioDTO): ResponseEntity<Usuario> {
        val usuario = usuarioService.criarUsuario(usuarioDTO);
        usuariosList.add(usuario);
        return ResponseEntity.status(201).body(usuario)
    }*/

    //Inserindo o usuario no banco de Dados
    @PostMapping
    fun cadastrarUsuario(@RequestBody novoUsuario: Usuario): ResponseEntity<Usuario> {
        val usuario = repositorio.save(novoUsuario)
        return ResponseEntity.status(201).body(usuario)
    }


    //Listando todos os usuarios do banco de dados
    @GetMapping
    fun listarUsuarios(): ResponseEntity<List<Usuario>> {
        val usuarios = repositorio.findAll()
        return if (usuarios.isEmpty()) {
            ResponseEntity.status(204).build()
        } else {
            ResponseEntity.status(200).body(usuarios)
        }
    }

    @PatchMapping("/login")
    fun login(@RequestBody autenticacao: AutenticacaoUsuarioDTO): ResponseEntity<Void> {
        val response = repositorio.autenticarUsuarioTRUE(autenticacao.email, autenticacao.senha)

        return if (response == 1) {
            ResponseEntity.status(200).build()
        } else
            ResponseEntity.status(404).build()
    }


    @PatchMapping("/{id}}")
    fun logoff(@PathVariable id: Int): ResponseEntity<Void> {
        val response = repositorio.autenticarUsuarioFALSE(id)

        return if (response == 1) {
            ResponseEntity.status(200).build()
        } else
            ResponseEntity.status(404).build()

    }
}

