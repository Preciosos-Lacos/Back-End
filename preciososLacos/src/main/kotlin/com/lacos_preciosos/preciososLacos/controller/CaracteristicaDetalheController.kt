package com.lacos_preciosos.preciososLacos.controller

import com.lacos_preciosos.preciososLacos.dto.cor.CadastroCorDTO
import com.lacos_preciosos.preciososLacos.dto.cor.DadosDetalheCorDTO
import com.lacos_preciosos.preciososLacos.dto.cor.CadastroCorModeloDTO
import com.lacos_preciosos.preciososLacos.dto.cor.UpdateCorDTO
import com.lacos_preciosos.preciososLacos.dto.imagem.ImagemDTO
import com.lacos_preciosos.preciososLacos.dto.tipoLaco.CadastroTipoLacoDTO
import com.lacos_preciosos.preciososLacos.dto.tipoLaco.DadosTipoLacoDTO
import com.lacos_preciosos.preciososLacos.model.CaracteristicaDetalhe
import com.lacos_preciosos.preciososLacos.repository.CaracteristicaDetalheRepository
import com.lacos_preciosos.preciososLacos.repository.CaracteristicaRepository
import com.lacos_preciosos.preciososLacos.service.CaracteristicaDetalheService
import com.lacos_preciosos.preciososLacos.validacao.ValidacaoException
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.util.UriComponentsBuilder

@RestController
@RequestMapping("/caracteristica-detalhe")
class CaracteristicaDetalheController(private val caracteristicaDetalheService: CaracteristicaDetalheService) {

    @PostMapping
    fun createCaracteristicaDetalhe(
        @RequestBody caracteristicaDetalhe: CaracteristicaDetalhe, uriBuilder: UriComponentsBuilder
    ):
            ResponseEntity<CaracteristicaDetalhe> {
        val caracteristica = caracteristicaDetalheService.create(caracteristicaDetalhe)
        return ResponseEntity.status(201).body(caracteristica)
    }

    @PostMapping("/cor")
    fun cadastroCor(
        @RequestBody cadastroCorDTO: CadastroCorDTO,
        uriBuilder: UriComponentsBuilder
    ): ResponseEntity<Any> {
        try {
            val criado = caracteristicaDetalheService.saveCor(cadastroCorDTO)
            // tentar incluir Location header se tivermos id
            val id = criado.id
            return if (id != null) {
                val uri = uriBuilder.path("/caracteristica-detalhe/cor/{id}").buildAndExpand(id).toUri()
                ResponseEntity.created(uri).body(criado)
            } else {
                ResponseEntity.status(201).body(criado)
            }
        } catch (ex: ValidacaoException) {
            return ResponseEntity.status(404).build()
        }
    }

    @PostMapping("/tipo-laco")
    fun cadastroTipoLaco(@RequestBody cadastroTipoLacoDTO: CadastroTipoLacoDTO): ResponseEntity<Any> {

        try {
            return ResponseEntity.ok(caracteristicaDetalheService.saveTipoLaco(cadastroTipoLacoDTO))
        } catch (ex: ValidacaoException) {
            System.out.println(ex.message)
            return ResponseEntity.status(404).build()
        }
    }

    @GetMapping("/tipo-laco")
    fun getAllTipoLaco(): ResponseEntity<List<DadosTipoLacoDTO>> {
        try {
            return ResponseEntity.ok(caracteristicaDetalheService.getAllTipoLaco())
        }
        catch (ex: Exception){
            return ResponseEntity.status(204).build()
        }
    }

    @DeleteMapping("/tipo-laco/{id}")
    fun deleteTipoLaco(@PathVariable id: Int): ResponseEntity<Any> {
        try {
            return ResponseEntity.ok(caracteristicaDetalheService.deleteTipoLaco(id))
        }
        catch (ex: Exception){
            return ResponseEntity.status(404).build()
        }
    }

    @PostMapping("/corModelo")
    fun associarCorModelo(@RequestBody cadastroCorDTO: CadastroCorModeloDTO): ResponseEntity<String> {
        return try {
            val msg = caracteristicaDetalheService.associateColor(cadastroCorDTO)
            ResponseEntity.ok(msg)
        } catch (ex: Exception) {
            ResponseEntity.status(500).body("Erro ao associar modelos à cor: ${ex.message}")
        }
    }

    @PutMapping("/corModelo")
    fun substituirAssociacoes(@RequestBody cadastroCorDTO: CadastroCorModeloDTO): ResponseEntity<String> {
        return try {
            val msg = caracteristicaDetalheService.replaceModelosForCor(cadastroCorDTO)
            ResponseEntity.ok(msg)
        } catch (ex: Exception) {
            ResponseEntity.status(500).body("Erro ao atualizar associações: ${ex.message}")
        }
    }

    @DeleteMapping("/corModelo/{idCor}")
    fun deletarPorCaracteristica(@PathVariable idCor: Int): ResponseEntity<String> {
        caracteristicaDetalheService.deleteByCaracteristicaId(idCor)
        return ResponseEntity.ok("Registros deletados com sucesso")
    }

    @GetMapping("/corModelo/{idCor}")
    fun buscarModeloIdsPorCaracteristica(@PathVariable idCor: Int): ResponseEntity<List<Int>> {
        val ids = caracteristicaDetalheService.getModeloIdsByCor(idCor)
        return ResponseEntity.ok(ids)
    }

    @PutMapping("/cor/{id}")
    fun editarCor(@PathVariable id: Int, @RequestBody updateCorDTO: UpdateCorDTO): ResponseEntity<String> {
        return try {
            ResponseEntity.ok(caracteristicaDetalheService.updateCor(id, updateCorDTO))
        } catch (ex: ValidacaoException) {
            ResponseEntity.status(404).body("Erro ao editar a cor: ${ex.message}")
        }
    }

    @GetMapping("/cor")
    fun buscarTodasAsCores(): ResponseEntity<Any> {
        return try {
            val cores = caracteristicaDetalheService.getTodasAsCores()
            if (cores.isEmpty()) {
                ResponseEntity.status(204).body("Nenhuma cor encontrada")
            } else {
                ResponseEntity.ok(cores)
            }
        } catch (ex: Exception) {
            ResponseEntity.status(500).body("Erro ao buscar cores: ${ex.message}")
        }
    }

    @GetMapping("/cor/{id}")
    fun buscarCor(@PathVariable id: Int): ResponseEntity<CaracteristicaDetalhe> {
        try {
            val cor = caracteristicaDetalheService.getCorById(id)
            return ResponseEntity.ok(cor)
        } catch (ex: ValidacaoException) {
            return ResponseEntity.status(404).build();
        }
    }

    @GetMapping("/cor/{id}/completo")
    fun buscarCorCompleto(@PathVariable id: Int): ResponseEntity<Any> {
        val dto = caracteristicaDetalheService.getCorCompleto(id)
        return ResponseEntity.ok(dto)
    }

    @DeleteMapping("/cor/{id}")
    fun excluirCor(@PathVariable id: Int): ResponseEntity<String> {
        return try {
            caracteristicaDetalheService.deleteCor(id)
            ResponseEntity.ok("Cor excluída com sucesso.")
        } catch (ex: ValidacaoException) {
            ResponseEntity.status(404).body("Erro ao excluir a cor: ${ex.message}")
        }
    }

    @GetMapping
    fun getAll(): ResponseEntity<List<CaracteristicaDetalhe>> {

        val caracteristicas = caracteristicaDetalheService.getAllCaracteristicas()

        return if (caracteristicas.isEmpty()) {
            ResponseEntity.status(204).build()
        } else {
            ResponseEntity.status(200).body(caracteristicas)
        }
    }

    @PutMapping("/{id}")
    fun update(@PathVariable id: Int, @RequestBody updated: CaracteristicaDetalhe):
            ResponseEntity<CaracteristicaDetalhe> {
        try {
            val detalhe = caracteristicaDetalheService.update(id, updated)
            return ResponseEntity.ok(detalhe)
        } catch (ex: ValidacaoException) {
            return ResponseEntity.status(404).build()
        }
    }

    @DeleteMapping("/{id}")
    fun delete(@PathVariable id: Int): ResponseEntity<Void> {
        try {
            caracteristicaDetalheService.delete(id)
            return ResponseEntity.status(404).build()
        } catch (ex: ValidacaoException) {
            return ResponseEntity.status(404).build()
        }
    }

    @PatchMapping("/{id}/imagem")
    @Tag(name = "Atualização de imagem")
    fun updateImagem(@PathVariable id: Int, @RequestBody @Valid imagemDTO: ImagemDTO):
            ResponseEntity<CaracteristicaDetalhe> {
        try {
            val detalhe = caracteristicaDetalheService.subirImagem(
                id,
                java.util.Base64.getDecoder().decode(imagemDTO.imagemBase64)
            )
            return ResponseEntity.ok(detalhe)
        } catch (ex: Exception) {
            return ResponseEntity.notFound().build()
        }
    }

    @GetMapping("/modelo/{idModelo}")
    fun getDetalhesPorModelo(@PathVariable idModelo: Int): ResponseEntity<List<CaracteristicaDetalhe>> {
        val lista = caracteristicaDetalheService.getDetalhesPorModelo(idModelo)
        return if (lista.isEmpty()) ResponseEntity.status(204).build() else ResponseEntity.ok(lista)
    }
}