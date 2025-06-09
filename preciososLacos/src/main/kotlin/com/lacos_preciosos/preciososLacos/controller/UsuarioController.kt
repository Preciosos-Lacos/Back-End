package com.lacos_preciosos.preciososLacos.controller

import com.lacos_preciosos.preciososLacos.dto.usuario.AtualizacaoUsuarioDTO
import com.lacos_preciosos.preciososLacos.dto.usuario.AutenticacaoUsuarioDTO
import com.lacos_preciosos.preciososLacos.dto.usuario.CadastroUsuarioDTO
import com.lacos_preciosos.preciososLacos.model.Usuario
import com.lacos_preciosos.preciososLacos.service.UsuarioService
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/usuarios")
class UsuarioController(private val repositorio: UsuarioService) {

    //Inserindo o usuario no banco de Dados
    @PostMapping
    @Tag(name = "Cadastro de usuário")
    fun cadastrarUsuario(@RequestBody novoUsuario: CadastroUsuarioDTO): ResponseEntity<Usuario> {
        val usuario = repositorio.save(novoUsuario)
        return ResponseEntity.status(201).body(usuario)
    }

    //Listando todos os usuarios do banco de dados
    @GetMapping
    @Tag(name = "Listagem de usuário")
    fun listarUsuarios(): ResponseEntity<List<Usuario>> {
        val usuarios = repositorio.findAll()
        return if (usuarios.isEmpty()) {
            ResponseEntity.status(204).build()
        } else {
            ResponseEntity.status(200).body(usuarios)
        }
    }


    @GetMapping("/pesquisar")
    @Tag(name = "Pesquisar usuário")
    fun listarPorNome(@RequestParam nomeCompleto: String): ResponseEntity<List<Usuario>> {
        val usuarioEncontrado = repositorio.findByNomeCompletoContains(nomeCompleto)

        return if (usuarioEncontrado.isEmpty()) {
            ResponseEntity.status(204).build()
        } else {
            ResponseEntity.status(200).body(usuarioEncontrado)
        }
    }

    //Atualizar o usuario no banco de dados
    @PutMapping("/{id}")
    @Tag(name = "Atualização de usuário")
    fun atualizarDados(@PathVariable id: Int, @RequestBody dto: AtualizacaoUsuarioDTO): ResponseEntity<Usuario> {
        val usuarioEncontrado = repositorio.findById(id)

        return if (usuarioEncontrado.isPresent()) {

            var usuario = usuarioEncontrado.get()

            usuario.nomeCompleto = dto.nome
            usuario.senha = dto.senha
            usuario.telefone = dto.telefone

            repositorio.save(usuario)
            ResponseEntity.status(200).body(usuario)
        } else {
            ResponseEntity.status(404).build()
        }
    }


    //Deletando usuario no banco de dados
    @DeleteMapping("/{id}")
    @Tag(name = "Exclusão de usuário")
    fun deletarUsuario(@PathVariable id: Int): ResponseEntity<Void> {
        val usuariosEncontrados = repositorio.findById(id)

        return if (usuariosEncontrados.isPresent) {
            repositorio.deleteById(id)
            ResponseEntity.status(200).build()
        } else {
            ResponseEntity.status(404).build()
        }
    }

    @PatchMapping("/login")
    @Tag(name = "Login de usuário")
    fun login(@RequestBody autenticacao: AutenticacaoUsuarioDTO): ResponseEntity<Void> {
        val response = repositorio.autenticarUsuarioTRUE(autenticacao.email, autenticacao.senha)

        return if (response == 1) {
            ResponseEntity.status(200).build()
        } else
            ResponseEntity.status(404).build()
    }


    @PatchMapping("/logoff/{id}")
    @Tag(name = "Logoff de usuário")
    fun logoff(@PathVariable id: Int): ResponseEntity<Void> {
        val response = repositorio.autenticarUsuarioFALSE(id)

        return if (response == 1) {
            ResponseEntity.status(200).build()
        } else
            ResponseEntity.status(404).build()
    }
}

