package com.lacos_preciosos.preciososLacos.controller

import com.lacos_preciosos.preciososLacos.dto.UsuarioDTO
import com.lacos_preciosos.preciososLacos.model.Usuario
import com.lacos_preciosos.preciososLacos.repository.UsuarioRepository
import com.lacos_preciosos.preciososLacos.service.UsuarioService
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
    fun listarUsuarios():ResponseEntity<List<Usuario>>{
        val usuarios = repositorio.findAll()
        return if(usuarios.isEmpty()){
            ResponseEntity.status(204).build()
        }else{
            ResponseEntity.status(200).body(usuarios)
        }
    }
  
}