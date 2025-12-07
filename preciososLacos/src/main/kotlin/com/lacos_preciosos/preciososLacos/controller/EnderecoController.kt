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
    ): ResponseEntity<com.lacos_preciosos.preciososLacos.dto.endereco.EnderecoResponseDTO> {
        return try {
            val updated = enderecoService.updateEndereco(id, dto)
            val dtoResp = com.lacos_preciosos.preciososLacos.dto.endereco.EnderecoResponseDTO(
                idEndereco = updated.idEndereco,
                cep = updated.cep,
                logradouro = updated.logradouro,
                bairro = updated.bairro,
                numero = updated.numero,
                complemento = updated.complemento,
                localidade = updated.localidade ?: "",
                uf = updated.uf ?: "",
                usuario = updated.usuario?.let { u ->
                    com.lacos_preciosos.preciososLacos.dto.usuario.UsuarioResponseDTO(
                        idUsuario = u.idUsuario,
                        nomeCompleto = u.nomeCompleto,
                        login = u.login,
                        senha = null,
                        cpf = u.cpf,
                        telefone = u.telefone,
                        role = u.role,
                        data_cadastro = u.data_cadastro.toString(),
                        fotoPerfil = u.getFotoPerfilBase64()
                    )
                }
            )
            ResponseEntity.ok(dtoResp)
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