package com.lacos_preciosos.preciososLacos.controller

import com.lacos_preciosos.preciososLacos.dto.endereco.EnderecoResponseDTO
import com.lacos_preciosos.preciososLacos.dto.usuario.UsuarioResponseDTO
import com.lacos_preciosos.preciososLacos.service.EnderecoService
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/usuarios")
class UsuarioEnderecoController(private val enderecoService: EnderecoService) {

    @GetMapping("/{idUsuario}/enderecos")
    @Tag(name = "Endereços do Usuário")
    fun getEnderecosDoUsuario(@PathVariable idUsuario: Int): ResponseEntity<List<EnderecoResponseDTO>> {
        val enderecos = enderecoService.getEnderecosByUsuario(idUsuario)
        val dtoList = enderecos.map { e ->
            EnderecoResponseDTO(
                idEndereco = e.idEndereco,
                cep = e.cep,
                logradouro = e.logradouro,
                bairro = e.bairro,
                numero = e.numero,
                complemento = e.complemento,
                localidade = e.localidade ?: "",
                uf = e.uf ?: "",
                usuario = e.usuario?.let { u ->
                    UsuarioResponseDTO(
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
        }
        return ResponseEntity.ok(dtoList)
    }
}
