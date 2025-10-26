package com.lacos_preciosos.preciososLacos.service

import com.lacos_preciosos.preciososLacos.dto.cor.CadastroCorDTO
import com.lacos_preciosos.preciososLacos.dto.cor.CadastroCorModeloDTO
import com.lacos_preciosos.preciososLacos.dto.cor.UpdateCorDTO
import com.lacos_preciosos.preciososLacos.dto.cor.UpdateCorModeloDTO
import com.lacos_preciosos.preciososLacos.model.CaracteristicaDetalhe
import com.lacos_preciosos.preciososLacos.model.Modelo
import com.lacos_preciosos.preciososLacos.repository.CaracteristicaDetalheRepository
import com.lacos_preciosos.preciososLacos.validacao.ValidacaoException
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service
import kotlin.collections.map

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

    fun associateColor(dto: CadastroCorModeloDTO): String {
        dto.listaModelos.forEach { idModelo ->
            caracteristicaDetalheRepository.insertModeloCor(idModelo, dto.id)
        }
        return "Associação entre cor ${dto.id} e modelos realizada com sucesso!"
    }

    fun deleteByCaracteristicaId(idCor: Int) {
        caracteristicaDetalheRepository.deleteByCaracteristicaId(idCor)
    }


    fun updateCor(id: Int, updateCorDTO: UpdateCorDTO): String {
        val cor = caracteristicaDetalheRepository.findById(id)
            .orElseThrow { ValidacaoException("Cor com ID $id não encontrada") }

        if(updateCorDTO.preco != cor.preco && updateCorDTO.preco != 0.0) {
            caracteristicaDetalheRepository.updateCor(id, updateCorDTO.preco)
        }

        return "Cor atualizada com sucesso!"
    }

    fun getTodasAsCores(): List<CadastroCorDTO> {

        val listaCaracteristicas: List<CaracteristicaDetalhe> = caracteristicaDetalheRepository.findAll();
        val listaCores = ArrayList<CadastroCorDTO>()

        listaCaracteristicas.forEach {
            cor ->
            var listModels: List<Modelo> = caracteristicaDetalheRepository.findAllModeloByCor(cor.idCaracteristicaDetalhe);
            var cor: CadastroCorDTO = CadastroCorDTO(cor.idCaracteristicaDetalhe, cor.descricao, cor.hexaDecimal, cor.preco, listModels);
            listaCores.add(cor);
        }
        return listaCores
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
