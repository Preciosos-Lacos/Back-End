package com.lacos_preciosos.preciososLacos.service

import com.lacos_preciosos.preciososLacos.dto.cor.CadastroCorDTO
import com.lacos_preciosos.preciososLacos.dto.cor.CadastroCorModeloDTO
import com.lacos_preciosos.preciososLacos.dto.cor.UpdateCorDTO
import com.lacos_preciosos.preciososLacos.dto.tipoLaco.CadastroTipoLacoDTO
import com.lacos_preciosos.preciososLacos.dto.tipoLaco.DadosTipoLacoDTO
import com.lacos_preciosos.preciososLacos.model.CaracteristicaDetalhe
import com.lacos_preciosos.preciososLacos.repository.CaracteristicaDetalheRepository
import com.lacos_preciosos.preciososLacos.validacao.ValidacaoException
import org.springframework.stereotype.Service
import java.util.Base64
import kotlin.collections.map

data class CorCompletaDTO(
    val idCaracteristicaDetalhe: Int,
    val descricao: String,
    val hexaDecimal: String,
    val preco: Double,
    val modelos: List<ModeloDTO>
)

data class ModeloDTO(
    val idModelo: Int,
    val nomeModelo: String
)

@Service
class CaracteristicaDetalheService(
    private val caracteristicaDetalheRepository: CaracteristicaDetalheRepository
) {

    fun create(caracteristicaDetalhe: CaracteristicaDetalhe): CaracteristicaDetalhe {
        caracteristicaDetalheRepository.save(caracteristicaDetalhe)
        return caracteristicaDetalhe
    }

    fun saveCor(cadastroCorDTO: CadastroCorDTO): CadastroCorDTO {
        // Salvar cor, ativo já é true na query do repositório
        caracteristicaDetalheRepository.saveCor(
            cadastroCorDTO.nomeDaCor,
            cadastroCorDTO.hexaDecimal,
            cadastroCorDTO.preco
        )

        // tentar localizar o registro recém-criado
        val criado = if (cadastroCorDTO.nomeDaCor != null && cadastroCorDTO.hexaDecimal != null)
            caracteristicaDetalheRepository.findByNomeAndHexa(cadastroCorDTO.nomeDaCor, cadastroCorDTO.hexaDecimal)
        else null
        return if (criado != null) {
            val modelos = caracteristicaDetalheRepository.findAllModeloByCor(criado.idCaracteristicaDetalhe)
            CadastroCorDTO(
                criado.idCaracteristicaDetalhe,
                criado.descricao,
                criado.hexaDecimal,
                criado.preco ?: 0.0,
                modelos,
                ativo = 1 // sempre 1 ao cadastrar
            )
        } else {
            // fallback: retornar o DTO enviado (sem id)
            cadastroCorDTO
        }
    }

    fun saveTipoLaco(cadastroTipoLacoDTO: CadastroTipoLacoDTO): String {

        val fotoBytes = java.util.Base64.getDecoder().decode(cadastroTipoLacoDTO.imagemBase64)
        caracteristicaDetalheRepository.saveTipoLaco(
            cadastroTipoLacoDTO.nome,
            cadastroTipoLacoDTO.preco,
            fotoBytes
        )

        return "Salvo"
    }

    fun getAllTipoLaco(): List<DadosTipoLacoDTO> {

        val listTipoLaco = caracteristicaDetalheRepository.getAllTipoLaco()

        if (listTipoLaco.isEmpty()) {
            throw RuntimeException("Lista vazia")
        }

        return listTipoLaco.map { row ->
            val imagemBytes = row["imagem"] as? ByteArray

            DadosTipoLacoDTO(
                id = row["id"] as Int,
                descricao = row["descricao"] as String,
                preco = (row["preco"] as Number).toDouble(),
                imagem = imagemBytes?.let {
                    Base64.getEncoder().encodeToString(it)
                } ?: "",
                modelos = (row["modelos"] as? String)
                    ?.split(",")
                    ?.map { it.trim() }
                    ?: emptyList()
            )
        }
    }

    fun updateTipoLaco(dto: CadastroTipoLacoDTO, id: Int): String {
        try {
            if (dto.nome.isNullOrBlank())
                throw IllegalArgumentException("Nome não pode ser vazio.")

            if (dto.preco == null || dto.preco < 0)
                throw IllegalArgumentException("Preço inválido.")

            // Trata imagem opcional
            val fotoBytes = try {
                dto.imagemBase64?.let {
                    Base64.getDecoder().decode(it)
                }
            } catch (ex: IllegalArgumentException) {
                throw IllegalArgumentException("Imagem Base64 inválida.")
            }

            val rows = caracteristicaDetalheRepository.updateTipoLaco(
                dto.nome,
                dto.preco,
                fotoBytes,
                id
            )

            return if (rows > 0) "Atualizado com sucesso" else "Nenhum registro atualizado"

        } catch (ex: Exception) {
            throw RuntimeException("Erro ao atualizar tipo laço: ${ex.message}")
        }
    }


    fun deleteTipoLaco(id: Int) {
        val tipoLaco = caracteristicaDetalheRepository.findById(id);

        if (tipoLaco.isEmpty()) {
            throw RuntimeException("Tipo de Laco não Encontrado")
        }
        caracteristicaDetalheRepository.deleteTipoLaco(id);
    }

    fun associateColor(dto: CadastroCorModeloDTO): String {
        dto.listaModelos.forEach { idModelo ->
            caracteristicaDetalheRepository.insertModeloCorIfNotExists(idModelo, dto.id)
        }
        return "Associação entre cor ${dto.id} e modelos realizada com sucesso!"
    }

    fun replaceModelosForCor(dto: CadastroCorModeloDTO): String {
        // deleta todas as associações e insere as novas (operacao idempotente do ponto de vista do resultado final)
        caracteristicaDetalheRepository.deleteByCaracteristicaId(dto.id)
        dto.listaModelos.forEach { idModelo ->
            caracteristicaDetalheRepository.insertModeloCorIfNotExists(idModelo, dto.id)
        }
        return "Associações atualizadas para cor ${dto.id}"
    }

    fun deleteByCaracteristicaId(idCor: Int) {
        caracteristicaDetalheRepository.deleteByCaracteristicaId(idCor)
    }


    fun updateCor(id: Int, updateCorDTO: UpdateCorDTO): String {
        val cor = caracteristicaDetalheRepository.findById(id)
            .orElseThrow { ValidacaoException("Cor com ID $id não encontrada") }

        if (updateCorDTO.preco != cor.preco && updateCorDTO.preco != 0.0) {
            caracteristicaDetalheRepository.updateCor(id, updateCorDTO.preco)
        }

        return "Cor atualizada com sucesso!"
    }

    fun getTodasAsCores(): List<CadastroCorDTO> {
        val listaCaracteristicas: List<CaracteristicaDetalhe> = caracteristicaDetalheRepository.findAllCores();
        val listaCores = ArrayList<CadastroCorDTO>()

        listaCaracteristicas.forEach { cor ->
            val listModels = caracteristicaDetalheRepository.findAllModeloByCor(cor.idCaracteristicaDetalhe)
            val ativoInt = if (cor.ativo == true) 1 else 0
            val dto = CadastroCorDTO(
                cor.idCaracteristicaDetalhe,
                cor.descricao,
                cor.hexaDecimal,
                cor.preco ?: 0.0, // previne erro de null
                listModels,
                ativo = ativoInt
            )
            listaCores.add(dto)
        }

        return listaCores
    }

    fun getCorCompleto(id: Int): CorCompletaDTO {
        val cor = caracteristicaDetalheRepository.findCorById(id)
            ?: throw ValidacaoException("Cor com esse ID não encontrada")
        val modelos = caracteristicaDetalheRepository.findAllModeloByCor(cor.idCaracteristicaDetalhe)
            .map { ModeloDTO(it.idModelo ?: 0, it.nomeModelo ?: "") }
        return CorCompletaDTO(
            cor.idCaracteristicaDetalhe ?: 0,
            cor.descricao ?: "",
            cor.hexaDecimal ?: "",
            cor.preco,
            modelos
        )
    }
    
        fun ativarCor(id: Int) {
            val cor = caracteristicaDetalheRepository.findById(id)
                .orElseThrow { ValidacaoException("Cor com ID $id não encontrada") }
            cor.ativo = true
            caracteristicaDetalheRepository.save(cor)
        }

    fun getModeloIdsByCor(idCor: Int): List<Int> {
        return caracteristicaDetalheRepository.findModeloIdsByCaracteristicaId(idCor)
    }

    fun getCorById(Id: Int): CaracteristicaDetalhe {

        val cor = caracteristicaDetalheRepository.findCorById(Id)

        if (cor == null) {
            throw ValidacaoException("Cor com esse ID não encontrada")
        }
        return cor
    }

    fun deleteCor(id: Int) {
        val cor = caracteristicaDetalheRepository.findById(id).orElse(null)
        if (cor == null) {
            throw ValidacaoException("Cor com ID $id não encontrada")
        }
        cor.ativo = false
        caracteristicaDetalheRepository.save(cor)
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

    fun getDetalhesPorModelo(idModelo: Int): List<CaracteristicaDetalhe> {
        return caracteristicaDetalheRepository.findAllByModelo(idModelo)
    }

}
