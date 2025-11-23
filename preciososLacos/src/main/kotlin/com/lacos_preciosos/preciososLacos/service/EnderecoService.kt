package com.lacos_preciosos.preciososLacos.service

import com.lacos_preciosos.preciososLacos.dto.endereco.CadastroEnderecoDTO
import com.lacos_preciosos.preciososLacos.model.Endereco
import com.lacos_preciosos.preciososLacos.repository.EnderecoRepository
import com.lacos_preciosos.preciososLacos.repository.UsuarioRepository
import com.lacos_preciosos.preciososLacos.validacao.ValidacaoException
import org.springframework.stereotype.Service

@Service
class EnderecoService(
    private val enderecoRepository: EnderecoRepository,
    private val usuarioRepository: UsuarioRepository
) {

    fun getEnderecosByUsuario(usuarioId: Int): List<Endereco> {
        return enderecoRepository.findByUsuario_IdUsuario(usuarioId)
    }

    fun createEndereco(dto: CadastroEnderecoDTO, usuarioId: Int): Endereco {
        val usuario = usuarioRepository.findById(usuarioId)
            .orElseThrow { ValidacaoException("Usuário não encontrado com ID: $usuarioId") }

        val endereco = Endereco(dto, usuario)
        return enderecoRepository.save(endereco)
    }

    fun getAllEnderecos(): List<Endereco> {
        return enderecoRepository.findAll()
    }

    fun getEnderecoById(id: Int): Endereco {
        return enderecoRepository.findById(id)
            .orElseThrow { ValidacaoException("Endereço não encontrado com ID: $id") }
    }

    fun updateEndereco(id: Int, dto: CadastroEnderecoDTO): Endereco {
        val endereco = enderecoRepository.findById(id)
            .orElseThrow { ValidacaoException("Endereço não encontrado com ID: $id") }

        endereco.cep = dto.cep
        endereco.logradouro = dto.logradouro
        endereco.bairro = dto.bairro
        endereco.numero = dto.numero
        endereco.complemento = dto.complemento
        endereco.localidade = dto.localidade
        endereco.uf = dto.uf

        return enderecoRepository.save(endereco)
    }

    fun deleteEndereco(id: Int) {
        if (!enderecoRepository.existsById(id)) {
            throw ValidacaoException("Endereço não encontrado com ID: $id")
        }
        enderecoRepository.deleteById(id)
    }
}