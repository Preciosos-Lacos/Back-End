package com.lacos_preciosos.preciososLacos.controller

import com.lacos_preciosos.preciososLacos.dto.modelo.AtualizacaoFotoDTO
import com.lacos_preciosos.preciososLacos.dto.modelo.CadastroModeloDTO
import com.lacos_preciosos.preciososLacos.dto.modelo.DadosDetalheModelo
import com.lacos_preciosos.preciososLacos.model.Modelo
import com.lacos_preciosos.preciososLacos.service.ModeloService
import com.lacos_preciosos.preciososLacos.validacao.ValidacaoException
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.util.UriComponentsBuilder

@RestController
@RequestMapping("/modelos")
class ModeloController(private val modeloService: ModeloService) {

    @PostMapping
    @Tag(name = "Cadastro de Modelo")
    fun createModelo(
        @RequestBody @Valid criacaoModeloDTO: CadastroModeloDTO,
        uriBuilder: UriComponentsBuilder
    ): ResponseEntity<DadosDetalheModelo> {

        var modelo = modeloService.createModelo(criacaoModeloDTO)
        var uri = uriBuilder.path("/modelos/{id}").buildAndExpand(modelo.idModelo).toUri()
        return ResponseEntity.created(uri).body(modelo)
    }

    @GetMapping
    @Tag(name = "Listagem de modelo")
    fun getAllModelos(): ResponseEntity<List<Modelo>> {

        val modelos = modeloService.getAllModelos()

        return if (modelos.isEmpty()) {
            ResponseEntity.status(204).build()
        } else {
            ResponseEntity.status(200).body(modelos)
        }
    }

    @GetMapping("/{id}")
    fun getOneModelo(@PathVariable id: Int): ResponseEntity<Modelo> {
        return ResponseEntity.of(modeloService.getOneModelo(id))
    }

    @GetMapping("/pesquisa-nome")
    @Tag(name = "Pesquisar modelo")
    fun getModeloByName(@RequestParam nome: String): ResponseEntity<Modelo> {
        val modelo = modeloService.getModeloByNome(nome)

        return ResponseEntity.of(modelo)
    }
    
    @PutMapping("/{id}")
    @Tag(name = "Atualização de modelo")
    fun updateModelo(
        @PathVariable id: Int,
        @RequestBody @Valid criacaoModeloDTO: CadastroModeloDTO
    ): ResponseEntity<DadosDetalheModelo> {

        try {
            return ResponseEntity.status(200).body(modeloService.updateModelo(id, criacaoModeloDTO))
        } catch (ex: ValidacaoException) {
            return ResponseEntity.status(404).build()
        }
    }

//    @PatchMapping("/{id}")
//    @Tag(name = "Atualização de foto")
//    fun updateFoto(@PathVariable id: Int, @RequestBody @Valid atualizacaoFotoDTO: AtualizacaoFotoDTO):
//            ResponseEntity<DadosDetalheModelo> {
//        try {
//            return ResponseEntity.status(200).body(modeloService.updateFoto(id, atualizacaoFotoDTO))
//        } catch (ex: ValidacaoException) {
//            return ResponseEntity.status(404).build()
//        }
//    }

    @DeleteMapping("/{id}")
    @Tag(name = "Exclusão de modelo")
    fun deleteModelo(@PathVariable id: Int): ResponseEntity<Void> {
        try {
            modeloService.deleteModelo(id)
            return ResponseEntity.status(204).build()
        } catch (ex: ValidacaoException) {
            return ResponseEntity.status(404).build()
        }
    }
}