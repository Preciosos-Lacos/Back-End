package com.lacos_preciosos.preciososLacos.controller

import com.lacos_preciosos.preciososLacos.dto.endereco.CadastroEnderecoDTO
import com.lacos_preciosos.preciososLacos.model.Endereco
import com.lacos_preciosos.preciososLacos.service.EnderecoService
import com.lacos_preciosos.preciososLacos.validacao.ValidacaoException
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.util.UriComponentsBuilder

@RestController
@RequestMapping("/enderecos")
class EnderecoController(private val enderecoService: EnderecoService) {
    @GetMapping("/usuario/{id}")
    @Tag(name = "Endereços por Usuário")
    fun getEnderecosByUsuario(@PathVariable id: Int): ResponseEntity<List<Endereco>> {
        val enderecos = enderecoService.getEnderecosByUsuario(id)
        return ResponseEntity.ok(enderecos)
    }

    @PostMapping
    @Tag(name = "Cadastro de Endereço")
    fun createEndereco(
        @RequestBody @Valid dto: CadastroEnderecoDTO,
        uriBuilder: UriComponentsBuilder
    ): ResponseEntity<Endereco> {
        val endereco = enderecoService.createEndereco(dto, dto.usuarioId)
        val uri = uriBuilder.path("/enderecos/{id}").buildAndExpand(endereco.idEndereco).toUri()
        return ResponseEntity.created(uri).body(endereco)
    }

    @GetMapping("/{id}")
    @Tag(name = "Detalhes de Endereço")
    fun getEnderecoById(@PathVariable id: Int): ResponseEntity<Endereco> {
        return try {
            ResponseEntity.ok(enderecoService.getEnderecoById(id))
        } catch (ex: ValidacaoException) {
            ResponseEntity.notFound().build()
        }
    }

    @PutMapping("/{id}")
    @Tag(name = "Atualização de Endereço")
    fun updateEndereco(
        @PathVariable id: Int,
        @RequestBody @Valid dto: CadastroEnderecoDTO
    ): ResponseEntity<Endereco> {
        return try {
            ResponseEntity.ok(enderecoService.updateEndereco(id, dto))
        } catch (ex: ValidacaoException) {
            ResponseEntity.notFound().build()
        }
    }

    @DeleteMapping("/{id}")
    @Tag(name = "Exclusão de Endereço")
    fun deleteEndereco(@PathVariable id: Int): ResponseEntity<Void> {
        return try {
            enderecoService.deleteEndereco(id)
            ResponseEntity.noContent().build()
        } catch (ex: ValidacaoException) {
            ResponseEntity.notFound().build()
        }
    }
}