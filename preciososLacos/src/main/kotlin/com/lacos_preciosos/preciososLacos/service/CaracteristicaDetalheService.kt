package com.lacos_preciosos.preciososLacos.service

import com.lacos_preciosos.preciososLacos.model.CaracteristicaDetalhe
import com.lacos_preciosos.preciososLacos.repository.CaracteristicaDetalheRepository
import com.lacos_preciosos.preciososLacos.repository.CaracteristicaRepository
import com.lacos_preciosos.preciososLacos.validacao.ValidacaoException
import org.springframework.stereotype.Service

@Service
class CaracteristicaDetalheService(
    private val caracteristicaDetalheRepository: CaracteristicaDetalheRepository,
    private val caracteristicaRepository: CaracteristicaRepository
) {

    fun create(caracteristicaDetalhe: CaracteristicaDetalhe): CaracteristicaDetalhe {
        // Optionally validate fkCaracteristica exists
        caracteristicaDetalhe.fkCaracteristica?.idCaracteristica?.let { id ->
            if (!caracteristicaRepository.existsById(id)) {
                throw ValidacaoException("Característica não encontrada com ID: $id")
            }
        }
        return caracteristicaDetalheRepository.save(caracteristicaDetalhe)
    }

    fun getAll(): List<CaracteristicaDetalhe> {
        return caracteristicaDetalheRepository.findAll()
    }

    fun update(id: Int, updated: CaracteristicaDetalhe): CaracteristicaDetalhe {
        val existente = caracteristicaDetalheRepository.findById(id)
            .orElseThrow { ValidacaoException("Detalhe de característica não encontrado com ID: $id") }

        existente.descricao = updated.descricao
        existente.fkCaracteristica = updated.fkCaracteristica

        return caracteristicaDetalheRepository.save(existente)
    }

    fun delete(id: Int) {
        if (!caracteristicaDetalheRepository.existsById(id)) {
            throw ValidacaoException("Detalhe de característica não encontrado com ID: $id")
        }
        caracteristicaDetalheRepository.deleteById(id)
    }
}