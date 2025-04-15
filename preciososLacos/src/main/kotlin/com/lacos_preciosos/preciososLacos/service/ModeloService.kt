package com.lacos_preciosos.preciososLacos.service

import com.lacos_preciosos.preciososLacos.dto.AtualizacaoFotoDTO
import com.lacos_preciosos.preciososLacos.dto.CadastroModeloDTO
import com.lacos_preciosos.preciososLacos.dto.DadosDetalheModelo
import com.lacos_preciosos.preciososLacos.model.Modelo
import com.lacos_preciosos.preciososLacos.repository.ModeloRepository
import com.lacos_preciosos.preciososLacos.validacao.ValidacaoException
import org.springframework.stereotype.Service
import java.util.*

@Service
class ModeloService(private val modeloRepository: ModeloRepository) {

    fun getAllModelos(): List<Modelo> {
        return modeloRepository.findAll()
    }

    fun getOneModelo(id: Int): Optional<Modelo> {
        return modeloRepository.findById(id)
    }

    fun getModeloByNome(nome: String): Optional<Modelo> {
        return modeloRepository.findByNomeModeloContainingIgnoreCase(nome)
    }

    fun createModelo(modeloDTO: CadastroModeloDTO): DadosDetalheModelo {
        val modelo = Modelo(modeloDTO)
        modeloRepository.save(modelo)
        return DadosDetalheModelo(modelo)
    }

    fun updateModelo(id: Int, modeloDto: CadastroModeloDTO): DadosDetalheModelo {

        var existe = modeloRepository.existsById(id)

        if (existe) {
            var modelo = Modelo(modeloDto)
            modelo.idModelo = id
            modeloRepository.save(modelo)
            return DadosDetalheModelo(modelo)
        } else {
            throw ValidacaoException("Modelo não encontrado")
        }
    }

    fun updateFoto(id: Int, fotoDTO: AtualizacaoFotoDTO): DadosDetalheModelo {
        var existe = modeloRepository.findById(id)

        if (existe.isEmpty) {
            throw ValidacaoException("Modelo não encontrado")
        } else {
            val modelo = existe.get()
            modeloRepository.updateFoto(id, fotoDTO.foto)
            modelo.foto = fotoDTO.foto
            return DadosDetalheModelo(modelo)
        }
    }

    fun deleteModelo(id: Int) {
        var existe = modeloRepository.existsById(id)

        if (existe) {
            modeloRepository.deleteById(id)
        } else {
            throw ValidacaoException("Modelo não encontrado")
        }
    }
}
