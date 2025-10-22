package com.lacos_preciosos.preciososLacos.service

import com.lacos_preciosos.preciososLacos.dto.cor.CadastroCorDTO
import com.lacos_preciosos.preciososLacos.dto.cor.UpdateCorDTO
import com.lacos_preciosos.preciososLacos.model.CaracteristicaDetalhe
import com.lacos_preciosos.preciososLacos.model.Modelo
import com.lacos_preciosos.preciososLacos.repository.CaracteristicaDetalheRepository
import com.lacos_preciosos.preciososLacos.repository.CaracteristicaRepository
import com.lacos_preciosos.preciososLacos.validacao.ValidacaoException
import org.springframework.data.jpa.domain.AbstractPersistable_.id
import org.springframework.stereotype.Service

@Service
class CaracteristicaDetalheService(
    private val caracteristicaDetalheRepository: CaracteristicaDetalheRepository
) {

    fun create(caracteristicaDetalhe: CaracteristicaDetalhe): CaracteristicaDetalhe {
        caracteristicaDetalheRepository.save(caracteristicaDetalhe)
        return caracteristicaDetalhe
    }

    fun saveCor(cadastroCorDTO: CadastroCorDTO): String {
        caracteristicaDetalheRepository.saveCor(
            cadastroCorDTO.nomeDaCor,
            cadastroCorDTO.hexaDecimal,
            cadastroCorDTO.preco
        )
        return ""
    }

    fun updateCor(id: Int, updateCorDTO: UpdateCorDTO): String {
        val cor = caracteristicaDetalheRepository.findById(id)
            .orElseThrow { ValidacaoException("Cor com ID $id não encontrada") }

        if(updateCorDTO.preco != cor.preco && updateCorDTO.preco != 0.0) {
            caracteristicaDetalheRepository.updateCor(id, updateCorDTO.preco)
        }

        return "Cor atualizada com sucesso!"
    }

    fun getTodasAsCores(): List<CaracteristicaDetalhe> {
        return caracteristicaDetalheRepository.findAll()
    }

    fun getCorById(Id: Int): CaracteristicaDetalhe {

        val cor = caracteristicaDetalheRepository.findCorById(Id)

        if (cor == null) {
            throw ValidacaoException("Cor com esse ID não encontrada")
        }
        return cor
    }

    fun deleteCor(id: Int) {
        if (!caracteristicaDetalheRepository.existsById(id)) {
            throw ValidacaoException("Cor com ID $id não encontrada")
        }
        caracteristicaDetalheRepository.deleteById(id)
    }

    fun getAllCaracteristicas(): List<CaracteristicaDetalhe> {
        return caracteristicaDetalheRepository.findAll();
    }

    fun update(id: Int, updated: CaracteristicaDetalhe): CaracteristicaDetalhe {
        val existing = caracteristicaDetalheRepository.findById(id).orElseThrow {
            ValidacaoException("Característica não encontrada")
        }

        val toSave = existing.copy(
            descricao = updated.descricao,
            caracteristica = updated.caracteristica
        )
        return caracteristicaDetalheRepository.save(toSave)
    }

    fun delete(id: Int) {
        val existing = caracteristicaDetalheRepository.findById(id).orElseThrow {
            ValidacaoException("Característica não encontrada")
        }
        caracteristicaDetalheRepository.deleteById(id)
    }

    fun subirImagem(id: Int, imagemBase64: ByteArray): CaracteristicaDetalhe {
        val detalhe = caracteristicaDetalheRepository.findById(id)
            .orElseThrow { RuntimeException("Característica detalhe não encontrada") }
        caracteristicaDetalheRepository.updateImagem(id, imagemBase64)
        detalhe.imagem = imagemBase64 // Atualiza o objeto em memória
        return detalhe
    }

}