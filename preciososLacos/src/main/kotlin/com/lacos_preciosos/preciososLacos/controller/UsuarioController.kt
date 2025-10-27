package com.lacos_preciosos.preciososLacos.controller

import com.lacos_preciosos.preciososLacos.dto.usuario.AtualizacaoUsuarioDTO
import com.lacos_preciosos.preciososLacos.dto.usuario.AtualizarSenhaDTO
import com.lacos_preciosos.preciososLacos.dto.usuario.AutenticacaoUsuarioDTO
import com.lacos_preciosos.preciososLacos.dto.usuario.CadastroUsuarioDTO
import com.lacos_preciosos.preciososLacos.model.Usuario
import com.lacos_preciosos.preciososLacos.repository.UsuarioRepository
import com.lacos_preciosos.preciososLacos.service.TokenService
import com.lacos_preciosos.preciososLacos.service.UsuarioService
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.ResponseEntity
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/usuarios")
class UsuarioController(
    private val repositorio: UsuarioRepository,
    private val manager: AuthenticationManager,
    private val tokenService: TokenService,
    private val passwordEncoder: PasswordEncoder,
    private val usuarioService: UsuarioService
) {

    //Inserindo o usuario no banco de Dados
    @PostMapping
    @Tag(name = "Cadastro de usuário")
    fun cadastrarUsuario(@RequestBody novoUsuario: CadastroUsuarioDTO): ResponseEntity<Usuario> {
        novoUsuario.senha = passwordEncoder.encode(novoUsuario.senha)
        val usuario = repositorio.save(Usuario(novoUsuario))
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
            ResponseEntity.status(204).build()
        } else {
            ResponseEntity.status(404).build()
        }
    }

    @PostMapping("/login")
    @Tag(name = "Login de usuário")
    fun login(@RequestBody autenticacao: AutenticacaoUsuarioDTO): ResponseEntity<String> {

        try {
            var authenticationToken: UsernamePasswordAuthenticationToken =
                UsernamePasswordAuthenticationToken(autenticacao.email, autenticacao.senha);

            var authentication = manager.authenticate(authenticationToken)

            val usuarioAutenticado = authentication.principal as Usuario

            var tokenJWT: String? = tokenService.gerarToken(usuarioAutenticado);

            return ResponseEntity.ok(tokenJWT)
        } catch (e: Exception){
            System.out.println(e.message);
            return ResponseEntity.status(403).body(e.message);
        }
    }

    @PatchMapping("/atualizar_senha")
    fun atualizarSenha (@RequestBody atualizarSenhaDTO: AtualizarSenhaDTO): ResponseEntity<Any> {
        try {
            return ResponseEntity.ok(usuarioService.atualizarSenha(atualizarSenhaDTO))
        } catch (e: Exception){
            return ResponseEntity.status(404).build()
        }
    }


}

